package com.wyw.eshop.cache.kafka;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.model.ShopInfo;
import com.wyw.eshop.cache.service.CacheService;
import com.wyw.eshop.cache.service.ProductInfoService;
import com.wyw.eshop.cache.service.ShopInfoService;
import com.wyw.eshop.cache.spring.SpringContext;
import com.wyw.eshop.cache.zk.ZookeeperSession;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;


public class KafkaMessageProcessor implements Runnable {
    private Logger logger = LoggerFactory.getLogger(KafkaMessageProcessor.class);

    private ConsumerRecord<String, String> consumerRecord;
    private CacheService cacheService;
    private ProductInfoService productInfoService;
    private ShopInfoService shopInfoService;

    public KafkaMessageProcessor(ConsumerRecord<String, String> consumerRecord) {
        this.consumerRecord = consumerRecord;
        cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        productInfoService = (ProductInfoService) SpringContext.getApplicationContext().getBean("productInfoService");
        shopInfoService = (ShopInfoService) SpringContext.getApplicationContext().getBean("shopInfoService");
    }

    @Override
    public void run() {
        String message = consumerRecord.value();
        logger.info("received message is : " + message);
        JSONObject messageJsonObject = JSONObject.parseObject(message);
        String serviceId = messageJsonObject.getString("serviceId");
        if (StringUtils.equals(serviceId, "productInfoService")) {
            processProductInfoChangeMessage(messageJsonObject);
        } else if (StringUtils.equals(serviceId, "shopInfoService")) {
            processShopInfoChangeMessage(messageJsonObject);
        }
    }

    private void processProductInfoChangeMessage(JSONObject messageJsonObject) {
        Integer productId = messageJsonObject.getInteger("productId");
        ProductInfo productInfo = productInfoService.getProductInfo(productId);
        cacheService.saveProductInfo2LocalCache(productInfo);

        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        zkSession.acquireDistributeLock(productId);
        ProductInfo exist = cacheService.getProductInfoFromRedisCache(productId);
        if(!ObjectUtils.isEmpty(exist)){
            LocalDateTime modifyTime = productInfo.getModifyTime();
            LocalDateTime existTime = exist.getModifyTime();
            if(modifyTime.isBefore(existTime)){
                logger.info("current date ["+productInfo.getModifyTime()+"]" +
                        " is before existed date ["+ exist.getModifyTime() +" ]");
                return;
            }
            logger.info("current date ["+productInfo.getModifyTime()+"]" +
                    " is after existed date ["+ exist.getModifyTime() +" ]");

        }else{
            logger.info("existed product info is null.....");
        }

        cacheService.saveProductInfo2RedisCache(productInfo);
        zkSession.releaseDistributeLock(productId);
    }

    private void processShopInfoChangeMessage(JSONObject messageJsonObject) {
        Integer productId = messageJsonObject.getInteger("productId");
        Integer shopId = messageJsonObject.getInteger("shopId");
        ShopInfo shopInfo = shopInfoService.getShopInfo(shopId);
        cacheService.saveShopInfo2LocalCache(shopInfo);
        cacheService.saveShopInfo2RedisCache(shopInfo);
    }
}
