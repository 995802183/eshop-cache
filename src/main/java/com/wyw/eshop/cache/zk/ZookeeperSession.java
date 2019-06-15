package com.wyw.eshop.cache.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperSession {
    private Logger logger = LoggerFactory.getLogger(ZookeeperSession.class);

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    private ZooKeeper zooKeeper;

    public ZookeeperSession(){
        try {
            this.zooKeeper = new ZooKeeper("192.168.74.133:2181",
                    50000,
                    new ZookeeperWatcher()
                    );
            connectedSemaphore.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Zookeeper session established ..... ");
    }


    private class ZookeeperWatcher implements Watcher{

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(Event.KeeperState.SyncConnected == watchedEvent.getState()){
                connectedSemaphore.countDown();
            }
        }
    }

    public void acquireDistributeLock(Integer productId){
        String path = "/product-lock-"+productId;

        try {
            zooKeeper.create(path,"".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            logger.info("success to acquire lock for product [id="+productId+"]");
        } catch (Exception e) {
            int count = 0;
            while (true){
                try {
                    Thread.sleep(20);

                    zooKeeper.create(path,"".getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e1) {
                    count++;
                    logger.info("the "+count + "times try to acqurie lock for "+path);
                    continue;
                }
                logger.info("success to acquire lock for product[id="+productId+"] after "+ count +"times try.");
            }
        }

    }

    public void acquireDistributeLock(String path){
        try {
            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
            logger.info("success to acquire lock for product ["+path+"]");
        } catch (Exception e) {
            int count = 0 ;
            while (true){
                try {
                    Thread.sleep(20);
                    zooKeeper.create(path,"".getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (Exception e1) {
                    count++;
                    logger.info("the "+count + "times try to acqurie lock for "+path);
                    continue;
                }
                logger.info("success to acquire lock for product[="+path+"] after "+ count +"times try.");
            }
        }
    }

    public Boolean acquireFastFailedDsitributedLock(String path){
        try {
            zooKeeper.create(path,"".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);
            logger.info("success to acquire lock for "+path);
            return true;
        } catch (Exception e) {
            logger.info("fail to acquire lock for "+path);
        }
        return false;
    }

    public void releaseDistributeLock(Integer productId){
        String path = "/product-lock-"+productId;
        try {
            zooKeeper.delete(path,-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void releaseDistributedLock(String path){
        try {
            zooKeeper.delete(path,-1);
            logger.info("release the lock for "+path);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public String getNodeData(String path){
        try {
            return String.valueOf(zooKeeper.getData(path,false,new Stat()));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNodeData(String path,String data){
        try {
            zooKeeper.setData(path,data.getBytes(),-1);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createNode(String path){
        try {
            zooKeeper.create(path,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Singleton{
        private static ZookeeperSession instance;
        static {
            instance = new ZookeeperSession();
        }
        public static ZookeeperSession getInstance(){
            return instance;
        }
    }
    public static ZookeeperSession getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }
}
