package org.example.redisstudy.redisTemplate.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisChapter2Test {
    @Autowired
    private RedisChapter2 chapter2;

    @Test
    void testEx1_1() {
        chapter2.addRecentProduct("1", "Redis");
        chapter2.addRecentProduct("1", "Study");
    }
    @Test
    void testEx1_2() {
        chapter2.getRecentProducts("1");
    }
    @Test
    void testEx1_3() {
        chapter2.addRecentProductUpgrade("1", "Study");
    }


    @Test
    void testEx2_1() {
        chapter2.addWaitingUser("1", "Redis");
        chapter2.addWaitingUser("1", "Study");
        chapter2.addWaitingUser("1", "JAVA");
        chapter2.addWaitingUser("1", "MySQL");
        chapter2.addWaitingUser("1", "JPA");
        chapter2.addWaitingUser("1", "Hello");
    }
    @Test
    void testEx2_2() {
        chapter2.popNextUser("1");
    }
    @Test
    void testEx2_3() {
        chapter2.getWaitingCount("1");
    }
}
