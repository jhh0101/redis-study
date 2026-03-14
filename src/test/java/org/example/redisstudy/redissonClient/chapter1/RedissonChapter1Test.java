package org.example.redisstudy.redissonClient.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class RedissonChapter1Test {

    @Autowired
    private RedissonChapter1 redissonChapter1;

    @Test
    void RLockTest() throws InterruptedException {
        int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonChapter1.bidWithLock(1L, 100);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        int finalPrice = redissonChapter1.getCurrentPrice();
        System.out.println("최종 입찰가 : " + finalPrice);
    }

    @Test
    void outLockTest() throws InterruptedException {
        int threadCount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redissonChapter1.bidWithOutLock( 100);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        int finalPrice = redissonChapter1.getCurrentPrice();
        System.out.println("최종 입찰가 : " + finalPrice);
    }

}
