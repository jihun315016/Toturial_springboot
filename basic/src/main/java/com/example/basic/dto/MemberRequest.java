package com.example.basic.dto;

import com.example.basic.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// DTO 비즈니스 로직은 DTO 내부가 아니라 서비스에서 작성한다.
// toEntity는 Entity 구조에 맞추어 객체를 생성하는 것이므로 예외적으로 DTO 내부에서 구현

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private String username;

    private String email;

    /**
     * 정적 팩토리 메서드: 요청 DTO를 엔티티로 변환합니다.
     * DTO의 책임 중 하나입니다.
     */
    public static Member toEntity(MemberRequest request) {
        return Member.builder()
                .username(request.username)
                .email(request.email)
                .build();
    }
}
