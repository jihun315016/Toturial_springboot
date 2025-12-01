package com.example.stomp.auth;

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
                if (!token.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationServiceException("Bearer 형식이 아닙니다.");
                }

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

                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("Role")));

                UserDetails userDetails = new User(claims.getSubject(), "", authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token");
        }
    }
}
