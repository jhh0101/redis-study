package org.example.redisstudy.redisTemplate.chapter5;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class RedisChapter5Test {
    @Autowired
    private RedisChapter5 chapter5;

    @Test
    void testEx1_1() {
        Map<String, String> userDetails = Map.of(
                "name", "test",
                "email", "test@test.com");
        chapter5.updateUserProfile("1", userDetails);
    }
    @Test
    void testEx1_2() {
        chapter5.getUserProfile("1");
    }
    @Test
    void testEx1_3() {
        chapter5.updateEmail("1", "new@test.test");
    }


}
