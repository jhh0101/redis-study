package org.example.redisstudy.redissonClient.chapter2;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect // AOP의 락 문지기
@Component // 현재 클래스를 빈으로 등록하고 관리
@RequiredArgsConstructor
public class DistributedLockAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String key = (String) CustomSpringELParser.getDynamicValue(
                signature.getParameterNames(),  // 어노테이션을 사용한 메서드에서 파라미터의 이름을 가져옴
                joinPoint.getArgs(),            // 어노테이션을 사용한 메서드에서 파라미터의 값을 가져옴
                distributedLock.key()
        );
        RLock lock = redissonClient.getLock(key);

        try {
            boolean isLocked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!isLocked) {
                //  throw new CustomException("입찰자가 너무 많음...");
                return null;
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
