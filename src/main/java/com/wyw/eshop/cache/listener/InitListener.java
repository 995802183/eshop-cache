package com.wyw.eshop.cache.listener;

import com.wyw.eshop.cache.prewarm.CachePrewarmThread;
import com.wyw.eshop.cache.rebuild.RebuildCacheThread;
import com.wyw.eshop.cache.spring.SpringContext;
import com.wyw.eshop.cache.zk.ZookeeperSession;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        SpringContext.setApplicationContext(applicationContext);
        ZookeeperSession.init();

        new Thread(new RebuildCacheThread()).start();

        new CachePrewarmThread().start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}