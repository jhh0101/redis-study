package org.example.redisstudy.redissonClient.chapter4;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class intercept {
    private static final ExpressionParser parser = new SpelExpressionParser();
    private final RedissonClient redissonClient;

    @Around("@annotation(rateLimit)")
    public Object intercept(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {

        // [Aspect 내부의 로직]
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 1. 메서드의 파라미터 이름과 실제 값들을 매칭시킴
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // 2. 어노테이션에 적힌 "#userId"라는 글자를 해석(Parse)함
        // 여기서 비로소 "#userId" 글자가 실제 숫자 123으로 바뀜
        String userId = parser.parseExpression(rateLimit.userId()).getValue(context, String.class);

        if (userId == null) {
//            throw new CustomException("사용자가 존재하지 않음....");
            throw new RuntimeException("사용자가 존재하지 않음....");
        }

        // 유저별로 구분하기 위해 유저 ID나 IP를 키에 조합
        String key = rateLimit.key() + ":" + userId;

        RRateLimiter limiter = redissonClient.getRateLimiter(key);

        // 설정: seconds초 동안 capacity만큼의 요청만 허용
        limiter.trySetRate(RateType.OVERALL, rateLimit.capacity(), rateLimit.seconds(), RateIntervalUnit.SECONDS);

        // 토큰 1개를 획득 시도 (있으면 true, 없으면 false)
        if (limiter.tryAcquire(1)) {
            return joinPoint.proceed();
        } else {
//            throw new CustomException("호출 속도가 너무 빠름...");
            throw new RuntimeException("호출 속도가 너무 빠름...");
        }

    }
}
