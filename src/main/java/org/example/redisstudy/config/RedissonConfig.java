package org.example.redisstudy.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();

        // redis 주소 입력 형식은 반드시 "redis://" 로 시작

        // .useSingleServer()은 redis 서버를 하나만 사용 / .useClusterServers()은 redis 서버 다수 사용
        config.useSingleServer()
              //  .setPassword()
                .setAddress(REDISSON_HOST_PREFIX + host + ":" + port);

        return Redisson.create(config);
    }

}
