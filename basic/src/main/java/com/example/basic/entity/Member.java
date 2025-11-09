package com.example.basic.entity;

import jakarta.persistence.*;
import lombok.*;

// @NoArgsConstructor(access = AccessLevel.PROTECTED)와 @AllArgsConstructor를 함께 사용하는 것이 JPA 표준 관례에 더 적합
// @Builder를 사용하는 경우 @AllArgsConstructor가 필요
// 엔티티에서 @Setter는 도메인 모델의 무결성을 해칠 수 있어 지양
// 비즈니스 로직을 포함한 메서드를 엔티티 내에 구현하는 것을 권장
@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용을 위해 private 생성자 허용
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 표준: 기본 생성자는 protected
@Getter
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    /**
     * 비즈니스 메서드: 이메일을 변경합니다. (도메인 로직)
     * Setter를 사용하지 않고 명시적인 메서드를 통해 데이터 변경을 유도합니다.
     */
    public void updateEmail(String newEmail) {
        // 실제 비즈니스 검증 로직이 여기에 들어갈 수 있습니다.
        if (newEmail == null || newEmail.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력 항목입니다.");
        }

        this.email = newEmail;
    }
}
