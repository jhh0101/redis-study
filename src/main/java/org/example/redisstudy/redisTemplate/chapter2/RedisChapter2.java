package org.example.redisstudy.redisTemplate.chapter2;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisChapter2 {
    private final StringRedisTemplate redisTemplate;

//    leftPush(key, value): 리스트의 맨 앞(왼쪽)에 데이터를 추가합니다. (최신 데이터가 0번 인덱스가 됨)
//    rightPush(key, value): 리스트의 맨 뒤(오른쪽)에 데이터를 추가합니다.
//    range(key, start, end): 특정 범위의 데이터를 가져옵니다. (0, -1을 넣으면 리스트 전체를 가져옵니다.)
//    trim(key, start, end): 리스트를 특정 범위만 남기고 나머지는 잘라버립니다. (개수 제한할 때 필수!)

    // 최근 본 상품 목록 (최대 5개)
    public void addRecentProduct(String userId, String productId) {
        String key = "user:recent:product:" + userId;
        redisTemplate.opsForList().leftPush(key, productId);
        redisTemplate.opsForList().trim(key, 0L, 4L);
        String print = redisTemplate.opsForList().getFirst(key);
        System.out.println("최근 본 상품 등록 : " + print);
    }

    // 리스트 범위 조회
    public List<String> getRecentProducts(String userId) {
        String key = "user:recent:product:" + userId;
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        System.out.println(list);
        return list;
    }

    // 중복 제거 버전
    public void addRecentProductUpgrade(String userId, String productId) {
        String key = "user:recent:product:" + userId;
        redisTemplate.opsForList().remove(key, 0, productId);
        redisTemplate.opsForList().leftPush(key, productId);
        redisTemplate.opsForList().trim(key, 0L, 4L);
        String print = redisTemplate.opsForList().getFirst(key);
        System.out.println("최근 본 상품 등록 : " + print);
    }

    // 대기열 등록
    public void addWaitingUser(String eventId, String userId) {
        String key = "event:waitlist:" + eventId;
        redisTemplate.opsForList().rightPush(key, userId);
    }

    // 대기자 호출(맨 처음 대기자 삭제)
    public String popNextUser(String eventId) {
        String key = "event:waitlist:" + eventId;

        String user = redisTemplate.opsForList().leftPop(key);

        System.out.println("대기자 호출 : " + user);
        return user;
    }

    // 현재 대기 인원
    public Long getWaitingCount(String eventId) {
        String key = "event:waitlist:" + eventId;
        Long count = redisTemplate.opsForList().size(key);
        List<String> list = redisTemplate.opsForList().range(key, 0, -1);
        System.out.println("현재 대기 인원 수 : " + count);
        System.out.println("현재 대기 인원 리스트 : " + list);
        return count;
    }


}