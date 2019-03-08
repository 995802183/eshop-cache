package com.wyw.eshop.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import tk.mybatis.spring.annotation.MapperScan;

@EnableCaching
@MapperScan("com.wyw.eshop.cache.mapper")
@SpringBootApplication
public class EshopCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopCacheApplication.class, args);
    }

}
