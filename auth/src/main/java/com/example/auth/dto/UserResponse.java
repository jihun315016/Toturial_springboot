package com.example.auth.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String userId;

    private String userName;
}