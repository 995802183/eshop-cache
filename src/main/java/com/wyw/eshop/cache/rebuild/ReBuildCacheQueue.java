package com.wyw.eshop.cache.rebuild;

import com.wyw.eshop.cache.model.ProductInfo;

import java.util.concurrent.ArrayBlockingQueue;

public class ReBuildCacheQueue {


    private ArrayBlockingQueue<ProductInfo> queue = new ArrayBlockingQueue<>(1000);
    public void putProductInfo(ProductInfo productInfo){
        try {
            queue.put(productInfo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ProductInfo takeProductInfo(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class Singleton{
        private static ReBuildCacheQueue instance;
        static {
            instance = new ReBuildCacheQueue();
        }

        public static ReBuildCacheQueue getInstance() {
            return instance;
        }
    }
    public static ReBuildCacheQueue getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }
}
