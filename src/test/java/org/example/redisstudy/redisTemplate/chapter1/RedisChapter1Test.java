package org.example.redisstudy.redisTemplate.chapter1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisChapter1Test {
    @Autowired
    private RedisChapter1 chapter1;

    @Test
    void testString() {
        chapter1.testString();
    }
    @Test
    void testIncrement() {
        chapter1.testIncrement("1");
    }
    @Test
    void testTtl() {
        chapter1.testTtl();
    }
    @Test
    void testEx1() {
        chapter1.canClickLike("1", "1");
        chapter1.canClickLike("1", "1");
    }
    @Test
    void testEx2() {
        chapter1.getFreePoint("1");
    }
    @Test
    void testEx3() {
        chapter1.verifyEventCode("1", "b43b929c5ce54bc29de4d33de460c121");
        chapter1.verifyEventCode("2", "479d50c52632405b98a2677144918cfa");
        chapter1.verifyEventCode("1", "NOT_SPECIAL_002");
    }
    @Test
    void testEx3_randomCode() {
        chapter1.randomCode("1");
        chapter1.randomCode("2");
    }
}
