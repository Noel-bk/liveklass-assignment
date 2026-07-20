# LiveKlass Backend Assignment

## 프로젝트 개요

과제 A — 수강 신청 시스템: LiveKlass의 온라인 강의 플랫폼을 가정하여 구현한 백엔드 과제입니다.

크리에이터(강사)는 강의를 등록하고 개설할 수 있으며, 클래스메이트(수강생)는 강의를 신청, 취소할 수 있습니다.
또한 자신의 수강 신청 내역을 조회할 수 있으며, 크리에이터는 자신의 강의에 대한 수강 신청 내역을 조회할 수 있습니다.

과제에서 요구한 필수 기능을 모두 구현하였으며, 일부 선택 기능도 추가 구현하였습니다.

---

## 기술 스택
```
- Java 17
- Spring Boot 4.0.7
- Spring Data JPA
- MySQL 8.4
- Gradle
- Docker / Docker Compose
- springdoc-openapi (Swagger UI)
- JUnit 5
```
---

## 실행 방법

### 1. MySQL 실행

```bash
docker compose up -d
```

초기 DB 생성이 안되는 경우
```bash
docker compose down -v
docker compose up -d
```

### 2. 애플리케이션 실행

```bash
./gradlew bootRun
```

또는 IntelliJ에서 `AssignmentApplication` 실행

### 3. Swagger

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

### 4. 초기 데이터

애플리케이션 실행 시 테스트 편의를 위해 초기 데이터가 최초 1회 생성됩니다.
- Creator 10명
- Classmate 10명
- Sample Course 1개

---

## API 목록 및 예시 (Swagger 참조)

### Course

- POST /courses
- PATCH /courses/{courseId}/open
- GET /courses
- GET /courses/{courseId}
- GET /courses/{courseId}/enrollments

### Enrollment

- POST /courses/{courseId}/enrollments
- PATCH /enrollments/{enrollmentId}/confirm
- PATCH /enrollments/{enrollmentId}/cancel
- GET /enrollments

---

## 데이터 모델 설명

### Creator

강의를 생성하고 개설할 수 있는 사용자

### Classmate

강의를 신청하는 사용자

### Course

강의 정보

- 제목
- 설명
- 가격
- 정원
- 현재 수강 신청 인원
- 강의 상태 (DRAFT -> OPEN -> CLOSED)
- 강의 시작일 / 종료일

### Enrollment

수강 신청 정보

- 신청 상태 (PENDING -> CONFIRMED -> CANCELLED)
- 신청 일시
- 확정 일시 (결제완료로 가정)
- 취소 일시

Creator (1) ---- (N) Course

Course (1) ---- (N) Enrollment

Classmate (1) ---- (N) Enrollment

---

## 요구사항 해석 및 가정

### 수강 취소

수강 확정(CONFIRMED) 상태에서만 취소 가능합니다.

취소 가능 기간은 확정/결제완료 시점(confirmedAt) 기준 7일 이내로 제한하였습니다.

---

### 강의 기간

종료일은 시작일보다 이전일 수 없습니다.

동일한 날짜(StartDate == EndDate)는 하루 과정으로 허용하였습니다.

데이터 마이그레이션 또는 과거 강의 등록 등의 경우를 위해 과거 시작일 등록을 허용하였습니다.

---

## 설계 결정과 이유

### Domain 중심 비즈니스 로직

비즈니스 규칙은 가능한 Entity 내부에서 처리하도록 설계하였습니다.

예를 들어 다음과 같은 상태 변경 로직은 Service가 아닌 Domain(Entity)에서 수행하도록 구현하였습니다.

ex)

- Course.open()
- Enrollment.confirm()
- Enrollment.cancel()

이를 통해 객체가 자신의 상태를 스스로 관리하도록 하였고, Service는 트랜잭션과 흐름 제어에 집중하도록 구성하였습니다.

---

### Factory Method 사용

Entity 생성 시 정합성을 유지하기 위해 Factory Method를 사용하였습니다.

ex)

- Course.create()
- Enrollment.create()

생성 시 기본 상태(PENDING, DRAFT 등)를 함께 설정하여 생성 로직이 여러 곳으로 분산되지 않도록 하였습니다.

---

### 인증 / 인가

별도의 인증 시스템은 구현하지 않고 간략히 Header를 이용하여 사용자 정보를 전달하도록 구현했습니다.

- X-Creator-Id
- X-Classmate-Id

또한 단순히 Header만 전달받는 것이 아니라 Repository 조회 시 소유자 조건까지 함께 조회하도록 구현하여 접근 권한을 검증하였습니다.

ex)

- findByIdAndCreatorId()
- findByIdAndClassmateId()

---

### 예외 처리

비즈니스 예외는 모두 `BusinessException`을 상속하도록 구성하였습니다.

각 예외는 `ErrorCode`를 함께 가지도록 설계하여 일관된 Error Response를 반환하도록 하였습니다.

또한 `GlobalExceptionHandler`를 통해 모든 예외를 중앙에서 처리하도록 구성하였습니다.

---

### 조회 성능 고려 (N+1 방지)

JPA 사용 시 발생할 수 있는 N+1 문제를 방지하기 위해 필요한 조회에는 `@EntityGraph`를 적용하였습니다.

ex)

- Course 조회 시 Creator Fetch
- Enrollment 조회 시 Course Fetch
- Course별 수강생 조회 시 Course + Classmate Fetch

필요한 연관 객체를 함께 조회하도록 하여 불필요한 추가 SQL 실행을 방지하였습니다.

---

### API 설계

조회 API는 누구나 사용할 수 있도록 공개하였으며,

등록, 개설, 신청, 취소, 확정과 같이 사용자의 권한이 필요한 API는 Header 기반 인증 정보를 사용하도록 설계하였습니다.

또한 Controller는 요청/응답 처리만 담당하고, 비즈니스 로직은 Service와 Domain으로 분리하여 계층 간 역할을 명확하게 유지하도록 구성하였습니다.

---

## 테스트 실행 방법

### 전체 테스트 실행

```bash
./gradlew test
```

주요 테스트 목록
- CourseServiceTest: 강의 생성 기능 검증
- EnrollmentServiceTest: 수강 신청 Lifecycle 검증
- EnrollmentConcurrencyTest: 동시성 환경에서 정원 초과 방지 검증

**EnrollmentConcurrencyTest**

테스트 시나리오:
```
정원(capacity)이 3명인 강의를 생성

10명의 수강생이 동시에 수강 신청 요청

최종 수강 인원은 최대 정원을 초과하지 않는지 검증
```

검증 항목:
```
Course.enrolledCount가 capacity를 초과하지 않음
Enrollment 데이터 개수가 capacity 이하
성공한 신청 수는 3건
실패한 신청은 CourseFullException 발생
```

### Swagger

```
http://localhost:8080/swagger-ui/index.html
```

### Postman

Swagger와 Postman을 이용하여 전체 API를 검증하였습니다.

동시성 테스트는 JUnit으로 별도 검증하였습니다.

---

## 미구현 / 제약사항

### Waitlist

선택 기능인 Waitlist는 구현하지 않았습니다.

현재 정원이 초과되면 CourseFullException을 반환하도록 구현되어 있습니다.

---

### 인증 / 인가

JWT 또는 Spring Security는 적용하지 않았으며,

Header 기반의 간단한 인증/인가를 적용하였습니다.

---

## AI 활용 범위

본 과제에서는 ChatGPT를 개발 보조 도구로 활용하였습니다.

AI를 활용한 범위는 다음과 같습니다.

```
- 설계 방향 및 아키텍처 검토
- 코드 리뷰 및 리팩토링 아이디어 검토
- JPA 사용 방식 및 EntityGraph 적용 검토
- 예외 처리 구조 개선
- 동시성 테스트 코드 작성 및 검증 방법 논의
- README 문서 초안 작성 및 개선
```

AI가 생성한 코드를 그대로 사용하지 않고, 프로젝트 요구사항에 맞게 수정 및 검증한 후 반영하였습니다.

---

## 추가 구현 기능

### 1. 신청 내역 페이지네이션

Spring Data JPA의 `Pageable`을 이용하여 신청 내역 조회 API에 페이지네이션을 적용하였습니다.

### 2. 강의별 수강생 목록 조회

크리에이터는 자신이 등록한 강의의 수강 신청 내역을 조회할 수 있도록 구현하였습니다.

### 3. 수강 취소 가능 기간 제한

수강 확정(`CONFIRMED`)(결제 완료) 이후 7일 이내에만 취소가 가능하도록 구현하였습니다.
