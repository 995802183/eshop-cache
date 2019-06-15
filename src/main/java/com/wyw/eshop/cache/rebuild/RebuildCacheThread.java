package com.wyw.eshop.cache.rebuild;

import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.service.CacheService;
import com.wyw.eshop.cache.spring.SpringContext;
import com.wyw.eshop.cache.zk.ZookeeperSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

public class RebuildCacheThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RebuildCacheThread.class);
    @Override
    public void run() {
        ReBuildCacheQueue reBuildCacheQueue = ReBuildCacheQueue.getInstance();
        ZookeeperSession zkSession = ZookeeperSession.getInstance();
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");

        while (true){
            ProductInfo productInfo = reBuildCacheQueue.takeProductInfo();
            zkSession.acquireDistributeLock(productInfo.getId());
            ProductInfo exist = cacheService.getProductInfoFromRedisCache(productInfo.getId());
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
            zkSession.releaseDistributeLock(productInfo.getId());
        }
    }
}
