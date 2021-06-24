package com.liubin.gulimall.product.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Description SpringCache配置
 * @Author liubin
 * @Date 2021/4/21 13:38
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class MyCacheConfig {

    @Bean
    RedisCacheConfiguration redisCacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存时间
        cacheConfig = cacheConfig.entryTtl(Duration.ofMinutes(60));
//        // 禁用缓存前缀
//        cacheConfig = cacheConfig.disableKeyPrefix();
        // 设置key的序列化器
        cacheConfig = cacheConfig.serializeKeysWith(RedisSerializationContext.
                SerializationPair.fromSerializer(new StringRedisSerializer()));
        // 设置value的序列化器
        cacheConfig = cacheConfig.serializeValuesWith(RedisSerializationContext.
                SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
        return cacheConfig;
    }
}
