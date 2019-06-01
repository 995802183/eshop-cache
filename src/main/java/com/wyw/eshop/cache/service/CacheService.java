package com.wyw.eshop.cache.service;

import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.model.ShopInfo;
import com.wyw.eshop.cache.service.impl.CacheServiceImpl;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface CacheService {
    @CachePut(value = CacheServiceImpl.CACHE_NAME, key = "'key_'+#productInfo.id")
    ProductInfo saveLocalCache(ProductInfo productInfo);

    @Cacheable(value = CacheServiceImpl.CACHE_NAME, key = "'key_'+#id")
    ProductInfo getLocalCache(Integer id);

    @CachePut(value = CacheServiceImpl.CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    @Cacheable(value = CacheServiceImpl.CACHE_NAME, key = "'product_info_'+#productId")
    ProductInfo getProductInfoFromLocalCache(Integer productId);

    @CachePut(value = CacheServiceImpl.CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    @CachePut(value = CacheServiceImpl.CACHE_NAME, key = "'shop_info_'+#shopId")
    ShopInfo getShopInfoFromLocalCache(Integer shopId);

    void saveProductInfo2RedisCache(ProductInfo productInfo);

    ProductInfo getProductInfoFromRedisCache(Integer productId);

    void saveShopInfo2RedisCache(ShopInfo shopInfo);

    ShopInfo getShopInfoFromRedisCache(Integer shopId);
}
