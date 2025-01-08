package com.kxj.artadmin.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.registerCustomCache("tokens", Caffeine.newBuilder()
                .maximumSize(1000)
                        .expireAfterWrite(7, TimeUnit.DAYS)
                .build());
        caffeineCacheManager.registerCustomCache("pass", Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(2, TimeUnit.HOURS)
                .build());
        caffeineCacheManager.registerCustomCache("data", Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES )
                .build());
        caffeineCacheManager.registerCustomCache("staticData", Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES )
                .build());
                return caffeineCacheManager;
    }


}
