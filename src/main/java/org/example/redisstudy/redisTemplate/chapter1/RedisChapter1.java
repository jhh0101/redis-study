package org.example.redisstudy.redisTemplate.chapter1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisChapter1 {
    private final StringRedisTemplate stringRedisTemplate;

    // 문자열 키:값 저장 / 조회
    public void testString() {
        var ops = stringRedisTemplate.opsForValue();
        String key = "study:name";
        ops.set(key, "value");
        String value = ops.get(key);
        System.out.println("키 : " + value);
    }

    // 조회수 증가
    public void testIncrement(String postId) {
        String key = "post:viewCount:" + postId;

        Long count = stringRedisTemplate.opsForValue().increment(key);
        System.out.println("조회수 : " + count);
    }

    // redis에 저장된 값 10초 후에 제거(삭제)
    public void testTtl() {
        String key = "user:email:test@test.com";

        stringRedisTemplate.opsForValue().set(key, "123456", Duration.ofSeconds(10));
        System.out.println("10초 전 저장 값 : " + stringRedisTemplate.opsForValue().get(key));

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        System.out.println("10초 후 저장 값 : " + stringRedisTemplate.opsForValue().get(key));
    }

    // 예제 문제 풀이
    // 중복 클릭 방지
    public boolean canClickLike(String userId, String postId) {
        String key = "like:lock:" + userId + ":" + postId;
        Boolean success = stringRedisTemplate.opsForValue().setIfAbsent(key, "locked", Duration.ofSeconds(3));
        if (Boolean.FALSE.equals(success)) {
            System.out.println("너무 자주 클릭하지 마세요!");
            return false;
        }
        System.out.println("클릭 성공");
        return true;
    }

    // 일일 방문 횟수 제한
    public void getFreePoint(String userId) {
        String key = "user:point:count:" + userId;
        Long count = stringRedisTemplate.opsForValue().increment(key);
        if (count <= 3) {
            System.out.printf("포인트 지급 완료! 현재 %d번 받음", count);
            return;
        }
        System.out.println("오늘은 더 이상 받을 수 없습니다.");
    }

    // 유저별 일회용 인증 코드 확인
    public void verifyEventCode(String userId, String inputCode) {
        String eventKey = "event:code:" + userId;
        String eventValue = stringRedisTemplate.opsForValue().get(eventKey);
        if (!inputCode.equals(eventValue)) {
            System.out.println("꽝!");
            return;
        }
        System.out.println("당첨입니다!");
        stringRedisTemplate.delete(eventKey);
    }

    public void randomCode(String userId) {
        String eventKey = "event:code:" + userId;
        String eventCode = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(eventKey, eventCode, Duration.ofMinutes(3));
        String eventValue = stringRedisTemplate.opsForValue().get(eventKey);

        System.out.printf("%s번 사용자의 코드번호는 %s입니다.(3분 후 만료)", userId, eventValue);
    }

}
