package org.example.redisstudy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    // redis host
    @Value("${spring.data.redis.host}")
    private String host;

    // redis port
    @Value("${spring.data.redis.port}")
    private int port;

    // redis password
    // @Value("${spring.data.redis.password}")
    // private String password;

    // redis 커넥션 풀 & 기본 설정
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // redis 기본 정보를 객체에 포장
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        // redisConfig.setPassword(password);
        //  redisConfig.setDatabase(0); // (0~15까지 방 선택 가능 / 최근엔 잘 사용 안함)

        // 통신할 때마다 매번 연결을 맺고 끊으면 느리므로, 스프링과 레디스 사이에 미리 개통해 둘 네트워크 연결(전화선)의 개수를 설정(커넥션 풀)
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
        // 트래픽 증가 시 조절 MinIdle은 다른 커넥션 수의 10% ~ 20%로 설정(레디스가 응답 없음 에러를 보낼 때 수치 조정)
        poolConfig.setMaxTotal(10);   // 최대 커넥션 수
        poolConfig.setMaxIdle(10);    // 대기 상태로 유지할 최대 커넥션 수
        poolConfig.setMinIdle(2);     // 대기 상태로 유지할 최소 커넥션 수

        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .commandTimeout(Duration.ofSeconds(2)) // 레디스가 2초 이상 응답이 없으면 즉시 에러 발생 (무한 대기 방지)
                .build();

        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    @SuppressWarnings("deprecation")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        // 자바 객체와 JSON간의 변환(객체 -> JSON / JSON -> 객체)
        ObjectMapper objectMapper = new ObjectMapper();
        // redis에 날짜가 "2026-03-13T16:04:28" 형식으로 저장이 가능하게 설정(설정을 안하면 [2026, 03, 13, 16, 04, 28]과 같은 배열로 저장됨)
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // redis에 저장 시 출처(클래스)남김(JSON -> 객체 변환 시 출처를 보고 변환)
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();
        objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);

        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Key를 String(문자열로 저장)
        template.setKeySerializer(new StringRedisSerializer());
        // Value를 JSON으로 저장
        template.setValueSerializer(jsonRedisSerializer);
        //HASH구조를 위와 같이 저장
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jsonRedisSerializer);

        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        // 키(문자열) - 값(문자열)만 빠르게 넣고 뺄 때 사용하는 최적화된 템플릿
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

}
