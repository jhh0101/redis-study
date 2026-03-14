package org.example.redisstudy.redissonClient.chapter3;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final RedissonClient redissonClient;

    // 스프링이 제공하는 웹소켓 전송 도구
    private final SimpMessageSendingOperations messageSendingOperations;

    @PostConstruct  // 백엔드 서버 실행 시 자동 실행
    public void subscribeToAuctionTopic() {
        RTopic topic = redissonClient.getTopic("auction:topic");

        // auction:topic 라는 키를 가진 상시 채널 메시지 응답 상시 대기
        topic.addListener(String.class, (channel, msg) -> {
            // 레디스에서 알림 오면 실행
            String[] parts = msg.split(":");
            String itemId = parts[0];
            String newPrice = parts[1];

            // "/topic/item/" + itemId로 엔드 포인트 설정. newPrice을 값으로 보냄
            messageSendingOperations.convertAndSend("/topic/item/" + itemId, newPrice);

            System.out.println(itemId + "번 상품 입찰가 변동! " + newPrice + "원");
        });
    }

}
