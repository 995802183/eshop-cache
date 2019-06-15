package com.wyw.eshop.cache.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.wyw.eshop.cache.model.ProductInfo;
import com.wyw.eshop.cache.service.CacheService;
import com.wyw.eshop.cache.service.ProductInfoService;
import com.wyw.eshop.cache.spring.SpringContext;
import com.wyw.eshop.cache.zk.ZookeeperSession;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

public class CachePrewarmThread extends Thread {

    @Override
    public void run(){
        CacheService cacheService = (CacheService) SpringContext.getApplicationContext().getBean("cacheService");
        ProductInfoService productInfoService = (ProductInfoService) SpringContext.getApplicationContext().getBean("productInfoService");
        ZookeeperSession zkSession = ZookeeperSession.getInstance();

        String taskidList = zkSession.getNodeData("/taskid-list");
        if(!ObjectUtils.isEmpty(taskidList)){
            String[] taskidListSplited = taskidList.split(",");
            for(String taskid : taskidListSplited){
                String taskidLockPath = "/taskid-lock-"+taskid;

                Boolean result = zkSession.acquireFastFailedDsitributedLock(taskidLockPath);
                if(!result){
                    continue;
                }
                String taskidStatusLockPath = "/taskid-status-lock-"+taskid;
                zkSession.acquireDistributeLock(taskidStatusLockPath);

                String taskidStatus = zkSession.getNodeData("/tskid-status-" + taskid);
                if(!StringUtils.isEmpty(taskidStatus)){
                    String productidList = zkSession.getNodeData("/task-host-product-list-" + taskid);
                    JSONArray productidJsonArray = JSONArray.parseArray(productidList);
                    for(Integer i=0;i<productidJsonArray.size();i++){
                        Integer productId = productidJsonArray.getInteger(i);
                        ProductInfo productInfo =
                                productInfoService.getProductInfo(productId);
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        cacheService.saveProductInfo2RedisCache(productInfo);
                    }
                    String taskidStatusPath =  "/tskid-status-" + taskid;
                    zkSession.createNode(taskidStatusPath);
                    zkSession.setNodeData(taskidStatusPath,"success");
                }

                zkSession.releaseDistributedLock(taskidStatusLockPath);
                zkSession.releaseDistributedLock(taskidLockPath);
            }
        }
    }
}
