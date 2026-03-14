package org.example.redisstudy.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());

        // 기본 캐시 설정 (유통기한 10분)
        RedisCacheConfiguration defaultTtl = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // 유통기한 10분
                .disableCachingNullValues() // 레디스에 null값 저장 방지
                .serializeValuesWith(jsonSerializer); // JSON 문자열로 번역해서 저장

        // 사용자 지정 캐시 설정 (유통기한 7일)
        RedisCacheConfiguration oneWeekTtl = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(7)) // 유통기한 7일
                .disableCachingNullValues() // 레디스에 null값 저장 방지
                .serializeValuesWith(jsonSerializer); // JSON 문자열로 번역해서 저장

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(defaultTtl)
                .withCacheConfiguration("auction:item", oneWeekTtl) // "auction:item"라는 키를 가진 데이터는 유통기한 1주일
                .build();

    }
}
