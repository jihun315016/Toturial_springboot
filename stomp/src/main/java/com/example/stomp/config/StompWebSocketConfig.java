package com.example.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:8081")
                // ws://가 아니라 http:// 엔드포인트를 사용할 수 있게 해주는 SockJS 라이브러리를 통한 요청 허용
                // 프론트엔드에서 SockJS를 사용할 것
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /publish/1 이런식으로 메시지 발행할 것
        // /publish로 시작하는 url 패턴으로 메시지가 발행되면 @Controller 객체의 @MessageMapping 메서드로 라우팅이 된다.
        registry.setApplicationDestinationPrefixes("/publish");

        // /topic/1 형태로 메시지 수신
        registry.enableSimpleBroker("/topic");
    }
}
