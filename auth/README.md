### 프로젝트
JWT 인증을 통한 토큰 발급 및 검증 기능을 작성한 프로젝트
<hr />

### 프로젝트 구조
- **auth**
    - JwtAuthFilter
    - JwtTokenProvider
- **configs**
    - SecurityConfig
- **controller**
    - UserController
- **dto**
    - UserRequest
    - UserResponse
<hr />

### 역할
- `JwtAuthFilter`
    - 토큰 유효성 검증 및 Spring Security에 인증 정보 저장
- `JwtTokenProvider`
    - 토큰 생성
- `SecurityConfig`
    - 애플리케이션의 필터 체인 정의
