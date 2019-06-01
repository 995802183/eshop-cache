package com.wyw.eshop.cache.controller;

import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.model.ShopInfo;
import com.wyw.eshop.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
    private Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    private CacheService cacheService;

    @RequestMapping("/testPutCache")
    public String testPutCache(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }

    @RequestMapping("/testGetCache")
    public ProductInfo testGetCache(Integer id) {
        return cacheService.getLocalCache(id);
    }


    @RequestMapping("/getProductInfo")
    public ProductInfo getProductInfo(Integer productId) {
        ProductInfo productInfo = null;
        productInfo = cacheService.getProductInfoFromRedisCache(productId);
        logger.info("redis cache productInfo:" + productInfo);

        if (ObjectUtils.isEmpty(productInfo)) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            logger.info("local cache productInfo:" + productInfo);
        }

        if (ObjectUtils.isEmpty(productInfo)) {

        }

        return productInfo;
    }

    @RequestMapping("/getShopInfo")
    public ShopInfo getShopInfo(Integer shopId) {
        ShopInfo shopInfo = null;
        shopInfo = cacheService.getShopInfoFromRedisCache(shopId);

        if (ObjectUtils.isEmpty(shopInfo)) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
        }

        if (ObjectUtils.isEmpty(shopInfo)) {

        }

        return shopInfo;
    }
}
