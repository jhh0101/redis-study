package org.example.redisstudy.config;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import io.lettuce.core.ReadFrom;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

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
    @Profile("local")
    public RedisConnectionFactory redisConnectionFactoryLocal() {
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
    @Profile("prod")
    public RedisConnectionFactory redisConnectionFactoryProd() {
        // 1. 클러스터 설정 객체 생성
//        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();

        // 2. 클러스터에 속한 노드들(마스터들)의 주소를 적어줍니다.
        // 모든 노드를 다 적을 필요는 없습니다.
        // 하나만 연결돼도 클러스터 내부에서 서로 정보를 공유해서 전체 지도를 알아냅니다.
        // Redis의 클러스터 슬롯은 16,384개 이다.
//        clusterConfig.addClusterNode(new RedisNode("IP주소", 6379));  // 데이터 샤딩(데이터 나눠 담기)은 레디스 내부에서 일어남
//        clusterConfig.addClusterNode(new RedisNode("IP주소", 6380));
//        clusterConfig.addClusterNode(new RedisNode("IP주소", 6381));

        // 처음 들어가는 host, port는 무조건 '마스터(Master)' 서버의 정보입니다.
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
                .master("mymaster")                       // sentinel.conf에서 설정한 이름
                .sentinel("192.168.219.1", 26379)
                .sentinel("192.168.219.1", 26380)
                .sentinel("192.168.219.1", 26381);  // 센티널은 과반수 투표를 위해 3, 5, 7... 홀수로 배치

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(10);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(2);

        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
                .poolConfig(poolConfig)
                .commandTimeout(Duration.ofSeconds(2))
                .readFrom(ReadFrom.REPLICA_PREFERRED) // 조회는 가급적 슬레이브에서 실행
                .build();

        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    // @SuppressWarnings("deprecation") // GenericJackson2JsonRedisSerializer 경고 제거(스프링부트 4.x.x에선 삭제될 예정)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        /*
        스프링 4.x 이전 설정 (import 패키지가 다름)

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

         */

        // 1. 출처(클래스 타입) 남기기용 Validator
        BasicPolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class)
                .build();

        // 2. Jackson 3 방식의 불변(Immutable) 매퍼 생성
        // - JavaTimeModule 자동 적용됨 (import 및 registerModule 불필요!)
        // - WRITE_DATES_AS_TIMESTAMPS 비활성화도 기본값이므로 설정 생략 가능!
        JsonMapper objectMapper = JsonMapper.builder()
                .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL)
                .build();

        //
        GenericJacksonJsonRedisSerializer jsonRedisSerializer = new GenericJacksonJsonRedisSerializer(objectMapper);

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
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        // 키(문자열) - 값(문자열)만 빠르게 넣고 뺄 때 사용하는 최적화된 템플릿
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
