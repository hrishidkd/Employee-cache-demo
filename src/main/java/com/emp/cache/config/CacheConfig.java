package com.emp.cache.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.emp.cache.entity.Employees;

@Configuration
public class CacheConfig {

//    @Bean
//    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
//        return (builder) -> builder
//                .withCacheConfiguration("product",
//                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
//                .withCacheConfiguration("customerCache",
//                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
//    }
//
//    @Bean
//    public RedisCacheConfiguration cacheConfiguration() {
//        return RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofMinutes(60))
//                .disableCachingNullValues()
//                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
    
    @Bean
    RedisTemplate<String, Employees> personRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        final var template = new RedisTemplate<String, Employees>();

        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Employees.class));

        return template;
    }

}