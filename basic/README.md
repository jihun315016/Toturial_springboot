## API 리스트
| 기능 | HTTP Method | Endpoint | 비고 |
| :--- | :---: | :--- | :--- |
| 회원 생성 | `POST` | `/api/members` | `@RequestBody` 사용 |
| 단일 조회 | `GET` | `/api/members/{id}` | `@PathVariable` 사용 |
| 전체 조회 | `GET` | `/api/members` | List 반환 |
| 정보 수정 | `PUT` | `/api/members/{id}` | 전체 수정 |
| 회원 삭제 | `DELETE` | `/api/members/{id}` | 204 No Content |
---
## Controller
### 클래스 어노테이션
`@RestController`
- RESTful 웹 서비스의 컨트롤러임을 표시
- 반환하는 객체를 JSON 형태로 자동 변환
`@RequestMapping`
- 기준 주소 설정
`@RequiredArgsConstructor`
- final 또는 @NonNull이 붙은 필드에 대해 생성자 자동생성

### 파라미터 어노테이션
`@RequestBody`
- HTTP 통신에서 JSON 형태의 body를 자바 객체로 변환
`@PathVariable`
- URL 경로에 포함된 변수 값 추출
    - /api/members/1
	- id가 1인 member 객체 추출
`@RequestParam`
- URL 쿼리 스트링 파라미터 추출
    - /api/members?name=kim

### ResponseEntity
HTTP 응답을 제어하기 위해 사용되는 객체
- **ResponseEntity.status(HttpStatus.CREATED)**
    - 상태코드 201
    - 성공적인 데이터 생성
- **ResponseEntity.ok()**
    - 상태코드 200
    - 성공적인 요청 처리
- **ResponseEntity.noContent()**
    - 상태코드 204
    - 처리는 완료되었으나, 분문에 담을 데이터가 없음

---
## Service
### 어노테이션
`@Transactional(readOnly = true)`
- 트랜잭션에 대해 해당 클래스를 읽기 전용으로 설정
`@Transactional`
- 읽기 전용으로 설정된 클래스에서 예외적으로 쓰기 작업 허용
- 메서드 대상 어노테이션

## Model
`@NoArgsConstructor`
- 파라미터가 없는 생성자 생성
`@AllArgsConstructor`
- 모든 필드를 인자로 받는 생성자 생성
`@Builder`
- 빌더 패턴을 구현하는 코드를 자동으로 생성
`@Data`
- 아래 어노테이션들을 한꺼번에 처리
	- Getter 
	- Setter
	- RequiredArgsConstructor
	- ToString