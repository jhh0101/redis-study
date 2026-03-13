package org.example.redisstudy.redisTemplate.chapter3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisChapter3Test {
    @Autowired
    private RedisChapter3 chapter3;

    @Test
    void testEx1_1() {
        chapter3.addTag("post123", "java");
        chapter3.addTag("post123", "spring");
        chapter3.addTag("post123", "redis");
        chapter3.addTag("post123", "java");
    }
    @Test
    void testEx1_2() {
        chapter3.getTags("post123");
    }
    @Test
    void testEx1_3() {
        chapter3.isTagExists("post123", "java");
        chapter3.isTagExists("post123", "python");
    }


}
