package com.example.basic.dto;

import com.example.basic.entity.Member;
import lombok.Getter;

// 데이터 불변성을 위해 Getter 어노테이션만 사용
// 클라이언트에 반환할 준비가 되면 프로그램 더 이상 값이 변경되지 않도록
@Getter
public class MemberResponse {
    private final Long id;
    private final String username;
    private final String email;

    // Entity를 받아 Response DTO를 생성하는 생성자
    public MemberResponse(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
}