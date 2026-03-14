package org.example.redisstudy.redissonClient.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class RedissonChapter2Test {

    @Autowired
    private RedissonChapter2 redissonChapter2;

    @Test
    void AopLockTest() throws InterruptedException {
        int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonChapter2.bidWithAopLock(1L, 100);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        int finalPrice = redissonChapter2.getCurrentPrice();
        System.out.println("최종 입찰가 : " + finalPrice);
    }

}
