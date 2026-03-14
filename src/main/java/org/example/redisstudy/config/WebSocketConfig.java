package org.example.redisstudy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버가 클라이언트에게 메시지를 보낼 때 사용하는 접두사(서버 -> 클라이언트)
        registry.enableSimpleBroker("/topic");

        // 클라이언트가 서버에게 메시지를 보낼 때 사용하는 접두사(클라이언트 -> 서버)
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓을 연결하기 위한 엔드 포인트 설정
        registry.addEndpoint("ws-auction")
                .setAllowedOriginPatterns("*")  // 테스트를 위한 모든 도메인 허용(실제 서비스에서는 도메인 또는 IP:포트번호로 연동)
                .withSockJS(); // 구현 브라우저 지원
    }
}
