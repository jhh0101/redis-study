package org.example.redisstudy.redisHash.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken")  // "refreshToken"은 키의 접두사(Prefix) ex) "refreshToken:"
public class RefreshToken {

    // DB의 기본키(PK)와 같은 역할. redis의 키로 "refreshToken:[userId]"가 됨. [userId]는 변수이므로 고정 값이 아님.
    @Id
    private String userId;

    // @Indexed을 남발하면 메모리 사용량이 매우 커지기 때문에 꼭 필요한 필드에만 사용
    @Indexed    // PK(userId)가 아닌 값으로 데이터를 찾고 싶을 때 사용
    private String token;   // 저장할 토큰 값.

    // 토큰의 유통기한. 단위는 초(Seconds).
    @TimeToLive
    private Long expiration;
}
