package org.example.redisstudy.redisTemplate.chapter5;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisChapter5 {
    private final StringRedisTemplate redisTemplate;

//    put(key, field, value): 한 칸에 데이터 넣기
//    get(key, field): 한 칸만 꺼내기
//    entries(key): 주머니 통째로(Map) 다 꺼내기

    // 유저 정보 저장
    public void updateUserProfile(String userId, Map<String, String> userDetails) {
        String key = "user:profile:" + userId;
        redisTemplate.opsForHash().putAll(key, userDetails);
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
    }

    // 유저 정보 전체 조회
    public Map<Object, Object> getUserProfile(String userId) {
        String key = "user:profile:" + userId;
        Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);
        System.out.println(userMap);
        return userMap;
    }

    // 이메일만 수정
    public void updateEmail(String userId, String email) {
        String key = "user:profile:" + userId;
        redisTemplate.opsForHash().put(key, "email", email);
    }
}