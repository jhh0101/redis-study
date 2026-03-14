package org.example.redisstudy.redissonClient.chapter3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedissonChapter3Test {

    @Autowired
    private RedissonChapter3 redissonChapter3;

    @Test
    void saveItemDesc_1() throws InterruptedException {
        redissonChapter3.saveAuctionItemDesc(1L, "딸기맛 아이스크림 입니다.");
    }

    @Test
    void saveItemDesc_2() throws InterruptedException {
        redissonChapter3.saveAuctionItemDesc(2L, "바닐라맛 아이스크림 입니다.");
    }

    @Test
    void getItemDesc() throws InterruptedException {
        redissonChapter3.getAuctionItemDesc(1L);
        redissonChapter3.getAuctionItemDesc(2L);
    }

}
