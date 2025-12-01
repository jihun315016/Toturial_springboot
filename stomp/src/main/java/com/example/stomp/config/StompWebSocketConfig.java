package com.example.stomp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// 웹소켓 지원 활성화
@EnableWebSocket
// 웹소켓 통신에서 메시지 브로커 사용 활성화
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    public StompWebSocketConfig(StompHandler stompHandler) {
        this.stompHandler = stompHandler;
    }

    // 클라이언트가 웹소켓 연결을 맺을 경로 지정
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 웹소켓 연결경로 지정
        registry.addEndpoint("/connect")
                .setAllowedOrigins("http://localhost:8081")
                // 서버 요청시 ws://와 http:// 모두 허용
                // 프론트엔드에서 SockJS 라이브러리 사용 허용
                // SockJS는 ws://가 아니라 http:// 엔드포인트를 사용할 수 있도록 지원
                .withSockJS();
    }

    // 메시지 송수신을 위한 접두사 지정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트가 서버로 메시지를 보낼 때 사용하는 접두사
        registry.setApplicationDestinationPrefixes("/publish");

        // 서버가 클라이언트에게 보낼 때 사용하는 접두사
        // 이 접두사로 시작하는 경로는 메시지 브로커가 처리
        registry.enableSimpleBroker("/topic");
    }

    // 클라이언트에서 서버로 들어오는 메시지 채널에 인터셉터 등록
    // * 채널 : 메시지가 흐르는 통로
    // * 인터셉터 : 메시지를 가로채서 추가적인 로직을 수행할 수 있게 해주는 객체
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // 클라이언트의 모든 STOMP 요청이 stompHandler를 거치도록 설정
        // 주로 인증, 인가 처리에 사용
        registration.interceptors(stompHandler);
    }
}
