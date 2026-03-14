package org.example.redisstudy.redissonClient.chapter2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedissonChapter2 {

    private int currentHighestPrice = 10000;

    public int getCurrentPrice() {
        return currentHighestPrice;
    }

    @DistributedLock(key = "'auction:lock:product:' + #productId")
    public void bidWithAopLock(Long productId, int addedPrice) {
        currentHighestPrice += addedPrice;
    }
}
