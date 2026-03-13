package org.example.redisstudy.redisTemplate.chapter3;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisChapter3 {
    private final StringRedisTemplate redisTemplate;

//      add(key, value): 데이터를 주머니에 넣습니다. (이미 있으면 무시됨)
//      members(key): 주머니에 있는 모든 데이터를 가져옵니다. (결과는 Set<String>으로 나옵니다.)
//      isMember(key, value): (중요!) 주머니에 이 데이터가 들어있는지 확인합니다. (T/F 반환)
//      remove(key, value): 특정 데이터를 주머니에서 뺍니다.
//      size(key): 주머니에 데이터가 총 몇 개 있는지 확인합니다.
//      intersect(key1, key2) 교집합: 둘 다 가지고 있는 것	공통 친구, 공통 관심사
//      union(key1, key2) 합집합: 둘 중 하나라도 가진 것	전체 카테고리, 중복 제거 목록
//      difference(key1, key2) 차집합: key1에는 있지만 key2에는 없는 것	내가 모르는 상대방의 관심사 (추천)

    // 게시글에 태그 추가
    public void addTag(String postId, String tag) {
        String key = "post:tags:" + postId;
        redisTemplate.opsForSet().add(key, tag);
    }

    // 해당 게시글에 달린 모든 태그 조회
    public Set<String> getTags(String postId) {
        String key = "post:tags:" + postId;
        Set<String> values = redisTemplate.opsForSet().members(key);
        System.out.println(values);
        return values;
    }

    // 해당 게시글에 특정 태그가 이미 달려있는지 확인
    public boolean isTagExists(String postId, String tag) {
        String key = "post:tags:" + postId;
        boolean isTag = Boolean.TRUE.equals((redisTemplate.opsForSet().isMember(key, tag)));
        String print = isTag ? "태그가 없습니다." : "태그가 존재합니다 : " + isTag;
        System.out.println(print);
        return isTag;
    }

    // 유저의 관심사 태그들을 저장
    public void addUserInterests(String userId, String... tags) {
        String key = "user:interests:" + userId;
        redisTemplate.opsForSet().add(key, tags);
    }

    // 두 유저가 동시에 좋아하는 태그 목록
    public Set<String> getCommonInterests(String userA, String userB) {
        String keyA = "user:interests:" + userA;
        String keyB = "user:interests:" + userB;
        Set<String> tags = redisTemplate.opsForSet().intersect(keyA, keyB);
        System.out.println(tags);
        return tags;
    }

    // 상대방은 좋아하지만 나는 아직 등록하지 않은 태그 목록
    public Set<String> getRecommendedInterests(String me, String other) {
        String keyA = "user:interests:" + me;
        String keyB = "user:interests:" + other;
        Set<String> tags = redisTemplate.opsForSet().difference(keyB, keyA); // keyA가 keyB를 대상으로 없는 것을 확인하고 싶을 때(1번 인수: keyB)
        System.out.println(tags);
        return tags;
    }
}