package com.example.stomp.controller;

import com.example.stomp.auth.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        // 로그인 성공했다고 가정

        String token = jwtTokenProvider.createToken("tempID");

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("Id", "tempID");
        loginInfo.put("token", token);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
