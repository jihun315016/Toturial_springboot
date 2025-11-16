package com.example.auth.controller;

import com.example.auth.auth.JwtTokenProvider;
import com.example.auth.dto.UserRequest;
import com.example.auth.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody UserRequest userRequest) {
        // 로그인 성공했다고 가정
        UserResponse user = UserResponse.builder()
                .userId(userRequest.getUserId())
                .userName(userRequest.getUserName())
                .build();

        String token = jwtTokenProvider.createToken(
                userRequest.getUserId()
                , userRequest.getUserName()
        );

        Map<String, Object> loginInfo = new HashMap<>();
        loginInfo.put("Id", user.getUserId());
        loginInfo.put("token", token);

        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
