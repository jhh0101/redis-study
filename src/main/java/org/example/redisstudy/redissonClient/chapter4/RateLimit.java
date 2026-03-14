package org.example.redisstudy.redissonClient.chapter4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String key();   // 구분자
    String userId();  // 구분자와 함께 사용할 유니크 키
    int capacity(); // 허용 횟수
    int seconds();  // 시간 단위(초)
}
