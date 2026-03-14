package org.example.redisstudy.redissonClient.chapter3;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedissonChapter3 {

    private final RedissonClient redissonClient;

    public void saveAuctionItemDesc(Long itemId, String description) {
        // auction:description 키를 객체에 저장
        RMap<Long, String> itemMap = redissonClient.getMap("auction:description");

        // 객체를 사용하여 키 호출 없이 값을 가져올 수 있음
        itemMap.put(itemId, description);
    }

    public String getAuctionItemDesc(Long itemId) {
        RMap<Long, String> itemMap = redissonClient.getMap("auction:description");

        String description = itemMap.get(itemId);
        System.out.println(itemId + "번 아이템의 설명 : " + description);

        return description;
    }



    // RTopic (Pub/Sub)
    // 경매 입찰 시
    public void successfulBid(Long itemId, int newPrice) {
        // 입찰 데이터 저장을 위한 DB 로직 & 유동성 데이터를 RMap으로 저장(입찰, 조회수 등)

        // auction:topic 이라는 키로 상시 채널을 가져옴
        RTopic topic = redissonClient.getTopic("auction:topic");

        // 메시지 생성 : itemId 상품의 입찰 가격이 newPrice원 으로 바뀜
        String message = itemId + ":" + newPrice;
        topic.publish(message);
    }

    /*
        @Cacheable(value = "auction:item", key = "#itemId")     :   [조회] 레디스에 있으면 꺼내주고, 없으면 DB에서 찾아서 레디스에 저장
        @CacheEvict(value = "auction:item", key = "#itemId")    :   [수정/삭제] 데이터가 바뀌었으니, 레디스에 있는 과거 데이터 삭제
        캐시 유통기한은 RedisCacheConfig 파일에서 .entryTtl(Duration.ofMinutes(10)) 등 별도 설정(10분 제한)
        유통기한은 @Cacheable, @CacheEvict 등 어노테이션에만 적용되며, 수동 생성한 RMap과 같은 레디스 데이터에는 적용되지 않음
     */


}
