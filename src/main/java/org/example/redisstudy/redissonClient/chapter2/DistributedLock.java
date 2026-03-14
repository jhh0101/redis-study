package org.example.redisstudy.redissonClient.chapter2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

// 메서드 위에서만 사용 가능
@Target(ElementType.METHOD)
// 프로그램 실행 중(RUNTIME)인 동안 계속 기억
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    // redis Key(필수)
    String key();

    // 락 기다리는 시간(기본 값 5초)
    long waitTime() default 5L;

    // 락 유지 시간(기본 값 3초)
    long leaseTime() default 3L;

    // 설정 시간 단위(SECONDS)
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
