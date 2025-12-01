package com.example.stomp.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final int experation;
    private Key SECRET_KEY;

    public JwtTokenProvider(@Value("${jwt.secretKey}") String secretKey, @Value("${jwt.expiration}") int experation) {
        this.experation = experation;
        this.SECRET_KEY = new SecretKeySpec(java.util.Base64.getDecoder().decode(secretKey), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(String userId) {
        Date now = new Date();

        // setSubject : 사용자 식별을 위한 키 값
        Claims claims = Jwts.claims().setSubject(userId);

        String token = Jwts.builder()
                // claims 객체를 Payload에 포함시킴
                .setClaims(claims)
                // 토큰 발급 시간
                .setIssuedAt(now)
                // 토큰 만료 시간
                // experation은 분 단위라고 가정하고 계산
                .setExpiration(new Date(now.getTime() + experation * 60 * 1000L))
                // 토큰 서명(위변조되지 않음을 보장)
                .signWith(SECRET_KEY)
                // 모든 클레임과 서명을 조합하여 JWT 문자열 반환
                .compact();

        return token;
    }
}
