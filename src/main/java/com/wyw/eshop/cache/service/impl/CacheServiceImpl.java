package com.wyw.eshop.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.model.ShopInfo;
import com.wyw.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public ProductInfo getLocalCache(Integer id) {
        return null;
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Integer productId) {
        return null;
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    @Override
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Integer shopId) {
        return null;
    }

    @Override
    public void saveProductInfo2RedisCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(productInfo));
    }

    @Override
    public ProductInfo getProductInfoFromRedisCache(Integer productId) {
        String key = "product_info_" + productId;
        String json = (String) redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
    }

    @Override
    public void saveShopInfo2RedisCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(shopInfo));
    }

    @Override
    public ShopInfo getShopInfoFromRedisCache(Integer shopId) {
        String key = "shop_info_" + shopId;
        String json = (String) redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(json, ShopInfo.class);
    }
}
