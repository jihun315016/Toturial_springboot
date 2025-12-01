package com.example.stomp.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StompController {
    // 이 코드 한 묶음 자체가 메시지브로커 역할을 해준다.(메시지 받고 전달)
    @MessageMapping("/{roomId}") // 클라이언트에서 특정 publish/roomiId 형태로 메시지 발생시 MessageMapping이 수신
    @SendTo("/topic/{roomId}") // 해당 룸id에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
    // DestinationVariable : MessageMapping 어노테이션으로 정의된 웹소켓 컨트롤러 내에서 사용
    public String sendMessage(@DestinationVariable Long roomId, String message) {
        System.out.println(message);
        return message;
    }
}
