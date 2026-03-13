package org.example.redisstudy.redisTemplate.chapter4;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisChapter4Test {
    @Autowired
    private RedisChapter4 chapter4;

    @Test
    void testEx1_1() {
        chapter4.addGameScore("1", "Kim", 1500);
        chapter4.addGameScore("1", "Lee", 2800);
        chapter4.addGameScore("1", "Park", 2100);
        chapter4.addGameScore("1", "Kim", 3500);
    }
    @Test
    void testEx1_2() {
        chapter4.getTopRankers("1");
    }
    @Test
    void testEx1_3() {
        chapter4.getMyRank("1", "Lee");
    }


}
