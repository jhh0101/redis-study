package org.example.redisstudy.redissonClient.chapter4;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedissonChapter4 {
    private final RedissonClient redissonClient;

    @RateLimit(key = "auction:bid", userId = "#userId", capacity = 1, seconds = 1)
    public void processBid(Long itemId, Long userId, int price) {
        // 입찰을 위한 로직.(경매장에서 다중 입찰 호출 방어)
    }
}
