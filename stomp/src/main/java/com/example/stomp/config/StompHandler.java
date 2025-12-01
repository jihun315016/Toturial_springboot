package com.example.stomp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// StompWebSocketConfig에서 설정한 인터셉터 역할 수행
@Component
public class StompHandler implements ChannelInterceptor {
    @Value("${jwt.secretKey}")
    private String secretKey;

    // 메시지 채널로 메시지가 전송되기 직전에 호출
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            // CONNECT 요청인 경우

            // 메시지 해더에서 Authorization 값 추출
            String barerToken = accessor.getFirstNativeHeader("Authorization");

            // 토큰에서 'Bearer ' 부분 제외한 값 추출
            String token = barerToken.substring(7);

            // JWT 유효성 검증
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // 토큰 검증 완료
        }

        return message;
    }
}