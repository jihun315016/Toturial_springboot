package com.example.stomp.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 스프링과 stomp는 기본적으로 세션 관리를 자동(내부적)으로 처리
// 연결, 해제 이벤트를 기록하고, 연결된 세션수를 실시간으로 확인할 목적으로 이벤트 리스너 생성(로그, 디버깅 목적)
@Component
public class StompEventListner {
    private final Set<String> sesseions = ConcurrentHashMap.newKeySet();

    // SessionConnectEvent 객체를 주입받는다고 선언하게 되면
    // 커넥션 요청이 발생할 때 이거 실행
    @EventListener
    public void connectHandle(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sesseions.add(accessor.getSessionId());
        System.out.println("connect seeion ID : " + accessor.getSessionId());
        System.out.println("totel session : " + sesseions.size());
    }

    @EventListener
    public void disconnectHandle(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        sesseions.remove(accessor.getSessionId());
        System.out.println("disconnect seeion ID : " + accessor.getSessionId());
        System.out.println("totel session : " + sesseions.size());
    }
}
