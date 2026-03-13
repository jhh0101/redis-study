package org.example.redisstudy.redisTemplate.chapter4;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisChapter4 {
    private final StringRedisTemplate redisTemplate;

//    add(key, value, score): 데이터를 특정 점수와 함께 주머니에 넣습니다. (이미 있으면 점수만 업데이트됩니다.)
//    reverseRange(key, start, end): 점수가 **높은 순서(내림차순)**대로 데이터를 가져옵니다. (랭킹 보기에 딱이죠!)
//    reverseRank(key, value): 특정 데이터의 현재 등수를 알려줍니다. (0등부터 시작하며, 내림차순 기준입니다.)
//    incrementScore(key, value, delta): 특정 데이터의 점수를 더하거나 뺍니다. (실시간 점수 업데이트에 유용!)

    // 실시간 게임 점수 랭킹 시스템
    // 유저의 점수를 추가하거나 업데이트
    public void addGameScore(String serverId, String userId, double score) {
        String key = "game:ranking:" + serverId;
        redisTemplate.opsForZSet().add(key, userId, score);
    }

    // 점수가 가장 높은 상위 3명의 유저 ID 조회
    public Set<String> getTopRankers(String serverId) {
        String key = "game:ranking:" + serverId;
        Set<String> ranker = redisTemplate.opsForZSet().reverseRange(key, 0, 2);
        System.out.println("상위 3명 : " + ranker);
        return ranker;
    }

    // 특정 유저의 현재 등수 조회
    public Long getMyRank(String serverId, String userId) {
        String key = "game:ranking:" + serverId;
        Long rank = redisTemplate.opsForZSet().reverseRank(key, userId);
        if (rank == null) {
            System.out.println(userId + "의 기록은 존재하지 않습니다.");
            return null;
        }
        System.out.println(userId + "의 순위는 " + (rank + 1) + "등 입니다.");
        return rank;
    }
}