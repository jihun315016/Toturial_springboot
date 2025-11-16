package com.example.auth.configs;

import com.example.auth.auth.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class Securityconfig {
    private final JwtAuthFilter jwtAuthFilter;

    public Securityconfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity, CorsConfigurationSource corsConfigurationSource) throws Exception {
        return httpSecurity
                // 웹은 같은 도메인이 아니면 통신이 안 됌, 실제로는 그럴 수 없음
                // 예외적으로 3000번(프론트)은 허용해주겠다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 (대비 안 함) <- 이건 필터에서 비활성화하고 코드에서 방어 가능, 보통은 비활성화함
                .httpBasic(AbstractHttpConfigurer::disable) // HTTP basic 비활성화, HTTP basic : 잘 안 쓰는 보안 인증 방법, 토큰 기반의 인증처리할거라서 비활성화 처리
                // 특정 url 패턴에 대해서는 authentication 객체를 요구하지 않음(인증처리 안 함)
                .authorizeHttpRequests(a -> a.requestMatchers("/user/login").permitAll().anyRequest().authenticated()) // 인증처리 안 하는 경로
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 방식을 사용하지 않겠다.
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // 토큰 검증
                .build();
    }
}
