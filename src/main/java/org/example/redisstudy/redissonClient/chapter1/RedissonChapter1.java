package org.example.redisstudy.redissonClient.chapter1;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedissonChapter1 {
    private final RedissonClient redissonClient;

    private int currentHighestPrice = 10000;

    public int getCurrentPrice() {
        return currentHighestPrice;
    }

    public void bidWithOutLock(int addPrice) {
        currentHighestPrice += addPrice;
    }

    public void bidWithLock(Long productId, int addPrice) {
        // 자물쇠 준비
        RLock lock = redissonClient.getLock("auction:lock:product:" + productId);

        try {
            // 파라미터: (기다릴 시간, 락 유지 시간, 시간 단위)
            // 자물쇠 획득(5초 대기, 3초 후 자동 해제)
            boolean isLocked = lock.tryLock(5, 3, TimeUnit.SECONDS);
            if (!isLocked) {
                //  throw new CustomException("입찰자가 너무 많음...");
                return;
            }
            //  DB에 데이터 저장...
            currentHighestPrice += addPrice;
            System.out.println(currentHighestPrice + "원 입찰 완료!");
        } catch (InterruptedException e) {
            throw new RuntimeException("입찰 중 에러 발생");
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
