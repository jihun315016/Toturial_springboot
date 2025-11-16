package com.example.auth.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRequest {
    private String userId;

    private String userName;

    private String password;
}
