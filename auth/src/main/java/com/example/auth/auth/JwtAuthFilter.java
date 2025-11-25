package com.example.auth.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(
            ServletRequest servletRequest
            , ServletResponse servletResponse
            , FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;

        // 요청 헤더에서 Authorization 값 추출
        String token = httpServletRequest.getHeader("Authorization");

        try {
            if (token != null) {
                // 토큰이 Bearer Schema 형식인지 확인
                // Bearer : 표준 JWT 인증 형식
                if (!token.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }

                // 'Bearer ' 접두사를 제거하고 순수한 JWT 값만 추출
                String jwtToken = token.substring(7);

                Claims claims = Jwts.parserBuilder()
                        // secretKey를 이용하여 토큰 서명(검증 보장)하도록 설정
                        .setSigningKey(secretKey)
                        .build()
                        // jwtToken 토큰에 담긴 정보 추출
                        .parseClaimsJws(jwtToken)
                        // claims 객체 반환
                        .getBody();

                List<GrantedAuthority> authorities = new ArrayList<>();

                // 추출된 claims에서 Role 키 값에 ROLE_ 접두사를 붙여 리스트에 추가
                // ROLE_ 는 관례적으로 붙여주는 것
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("Role")));

                // JWT 클레임으로 Spring Security 표준 사용자 정보(UserDetails) 생성
                // Spring Security는 로그인 처리 후
                // 사용자 정보를 UserDetails 타입으로 가져와 사용
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                // UserDetails 객체를 Security Context에 인증 상태 등록
                // Spring Security는 Authentication 객체를 검사하므로 Authentication 타입으로 등록
                // new UsernamePasswordAuthenticationToken 매개 변수
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // 다음 필터로 넘어감
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }
}
