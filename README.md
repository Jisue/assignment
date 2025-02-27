
# 개요

### DB 구성

- DataBase는 h2를 사용하였습니다.
- resource/sql 폴더에서 DB정보와 테스트 데이터를 넣어두었습니다.

```markdown
실행 후 `localhost:8989/h2-console` 접속하여 아래 정보로 디비 접속해 데이터 확인 가능합니다.
- Driver Class: `org.h2.Driver`
- JDBC URL: `jdbc:h2:mem:testdb`
- User Name: `sa`
```


### 추가 설명
- JDK 17 버전과 Spring Boot 3.4.3 버전을 사용했습니다.
- 회원은 일반 주문 회원(ORDER), 라이더 배달 회원(DELIVERY)으로 2개의 타입을 가집니다.
    - 일반 주문 회원이 주문한 정보 중 주문 생성 및 수정은 ORDER회원이, 주문 상태 수정은 DELIVERY회원이 접근 가능할 수 있도록 확장성을 생각하여 정의하였습니다.
- 회원/로그인을 제외한 API는 JWT 인증이 필요합니다
    - `Authorization` 헤더에 Bearer 토큰을 포함해야 합니다.
    - 만약, 토큰 재발급을 위한 refresh 토큰 관리를 추가한다면 redis를 사용해 관리할 생각입니다.
- 회원 인증/인가는 스프링 시큐리티를 사용하였습니다.

---

# API 명세서

# 📌 회원

## 1. 회원가입 API

- 사용자가 아이디와 비밀번호 이름를 입력하여 회원가입합니다.
- 회원은 일반 주문 회원(ORDER), 라이더 배달 회원(DELIVERY)으로 2개의 타입을 가집니다.

### 요청 (Request)

- **URL**: `/api/auth/sign-up`
- **Method**: `POST`
- **Content-Type**: `application/json`

### 요청 바디 (Request Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `id` | String | O | 사용자 ID (중복 불가) |
| `name` | String | O | 사용자 이름 |
| `password` | String | O | 비밀번호 (대문자, 소문자, 숫자/특수문자 포함, 최소 12자) |
| `type` | String | O | 사용자 유형 (`ORDER` 또는 `DELIVERY`) |

### 요청 예시

```json
{
  "id": "jisue",
  "name": "김지수",
  "password": "Barogo123!@#",
  "type": "ORDER"
}
```

### 응답 (Response)

- **HTTP Status Code**: `200 OK`

### 응답 바디 (Response Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `code` | int | O | 응답 코드 (0: 성공, 그 외: 실패) |
| `message` | String | O | 응답 메시지 |
| `data` | Object | O | 응답 데이터 |
| ├`token` | Object | O | JWT 토큰 정보 |
- `token` 구조

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `accessToken` | String | O | 액세스 토큰 (JWT) |
| `refreshToken` | String | O | 리프레시 토큰 (JWT) |

### 응답 예시

```json
{
    "code": 0,
    "message": "성공",
    "data": {
        "token": {
            "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiJqaXN1ZTQiLCJ1c2VyTmFtZSI6Iuq5gOyngOyImCIsInJvbGVDb2RlIjoxMCwiaWF0IjoxNzQwNTcxMjYyLCJleHAiOjE3NDA1ODkyNjJ9.-8egq59oP3rJi4z_J6z7fFpeAdWiMoJERM3ZMV3oGSwmg2JRQYLFstTbz0w4X99oLSvIv4ijE_ZWx3maK0gJqg",
            "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiJqaXN1ZTQiLCJpYXQiOjE3NDA1NzEyNjIsImV4cCI6MTc0MDYwNzI2Mn0.Bc2ClSL7LeiIZ2eQgBS-Tzbw2Z2uIvMWm71lyPn7xz1cp8bk_kORT86_K3AsbHXQ8OOyaR7ec2xdqENpDklCDw"
        }
    }
}
```

---

### 에러 응답 (Error Response)

### 이미 존재하는 ID

- **HTTP Status Code**: `400 Bad Request`

```json
{
    "code": 400,
    "message": "이미 가입된 ID입니다.",
    "data": null
}

```

### 비밀번호 규칙 불일치

- **HTTP Status Code**: `400 Bad Request`

```json
{
    "code": 400,
    "message": "비밀번호는 대문자, 소문자, 숫자 또는 특수문자 중 3가지 이상을 포함해야 하며 최소 12자 이상이어야 합니다.",
    "data": null
}
```

---

## 2. 로그인 API

- 사용자가 아이디와 비밀번호를 입력하여 로그인합니다.

### 요청 (Request)

- **URL**: `/api/auth/sign-in`
- **Method**: `POST`
- **Content-Type**: `application/json`

### 요청 바디 (Request Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `id` | String | O | 사용자 ID |
| `password` | String | O | 사용자 비밀번호 |

### 요청 예시

```json
{
  "id": "jisue",
  "password": "Barogo123!@#"
}
```

---

### 응답 (Response)

- **HTTP Status Code**: `200 OK`

### 응답 바디 (Response Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `code` | int | O | 응답 코드 (0: 성공, 그 외: 실패) |
| `message` | String | O | 응답 메시지 |
| `data` | Object | O | 응답 데이터 |
| ├`token` | Object | O | JWT 토큰 정보 |
- `token` 구조

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `accessToken` | String | O | 액세스 토큰 (JWT) |
| `refreshToken` | String | O | 리프레시 토큰 (JWT) |

### 응답 예시

```json
{
    "code": 0,
    "message": "성공",
    "data": {
        "token": {
            "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiJqaXN1ZTQiLCJ1c2VyTmFtZSI6Iuq5gOyngOyImCIsInJvbGVDb2RlIjoxMCwiaWF0IjoxNzQwNTcxMjYyLCJleHAiOjE3NDA1ODkyNjJ9.-8egq59oP3rJi4z_J6z7fFpeAdWiMoJERM3ZMV3oGSwmg2JRQYLFstTbz0w4X99oLSvIv4ijE_ZWx3maK0gJqg",
            "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VySWQiOiJqaXN1ZTQiLCJpYXQiOjE3NDA1NzEyNjIsImV4cCI6MTc0MDYwNzI2Mn0.Bc2ClSL7LeiIZ2eQgBS-Tzbw2Z2uIvMWm71lyPn7xz1cp8bk_kORT86_K3AsbHXQ8OOyaR7ec2xdqENpDklCDw"
        }
    }
}
```

---

# 📌 배달 주문

## 공통 사항

- JWT 인증이 필요하므로 `Authorization` 헤더에 Bearer 토큰을 포함해야 합니다.

### 1. 배달 조회 API

- 로그인한 주문 유저의 배달 주문 조회 리스트를 반환합니다.
- 조회 시작 날짜는 조회 종료 날짜보다 이후일 수 없으며, 최대 3일까지만 조회 가능합니다.
- 리스트는 생성 날짜 기준 내림차순 정렬되어 반환됩니다.

### 요청 (Request)

- **URL**: `/api/order/list`
- **Method**: `GET`
- **Content-Type**: `application/json`

### 요청 파라미터 (Request Parameters)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `startDate` | String (`yyyy-MM-dd`) | O | 조회 시작 날짜 |
| `endDate` | String (`yyyy-MM-dd`) | O | 조회 종료 날짜 |

### 요청 예시

```
GET /api/order/list?startDate=2024-02-25&endDate=2024-02-27
```

### 응답 (Response)

- **HTTP Status Code**: `200 OK`

### 응답 바디 (Response Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `code` | int | O | 응답 코드 (0: 성공, 그 외: 실패) |
| `message` | String | O | 응답 메시지 |
| `data` | Object | O | 응답 데이터 |
| ├`totalCount` | int | O | 총 주문 개수 |
| ├`orderList` | Array | O | 주문 목록 |
- `orderList`  배열 구조

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `orderId` | String | O | 주문 ID |
| `userName` | String | O | 주문자 이름 |
| `receiverName` | String | O | 수령인 이름 |
| `paymentType` | String | O | 결제 유형 (`CARD`, `CASH`) |
| `orderAmount` | Long | O | 주문 금액 (원) |
| `paymentAmount` | Long | X | 결제 금액 (원) |
| `address` | String | O | 배송지 주소 |
| `deliveryStatus` | String | O | 배송 상태 (`PENDING`, `STARTED`, `COMPLETED`) |
| `createDate` | String | O | 주문 생성 날짜 (`yyyy-MM-dd'T'HH:mm:ss`) |

### 응답 예시

```json
{
  "code": 0,
  "message": "성공",
  "data": {
    "totalCount": 2,
    "orderList": [
      {
        "orderId": "000001",
        "userName": "김지수",
        "receiverName": "김지수",
        "paymentType": "CARD",
        "orderAmount": 20000,
        "paymentAmount": 20000,
        "address": "서울특별시",
        "deliveryStatus": "PENDING",
        "createDate": "2025-02-26T00:00:00"
      },
      {
        "orderId": "000002",
        "userName": "김지수",
        "receiverName": "김지수",
        "paymentType": "CASH",
        "orderAmount": 30000,
        "paymentAmount": 30000,
        "address": "경기도",
        "deliveryStatus": "COMPLETED",
        "createDate": "2025-02-24T00:00:00"
      }
    ]
  }
}
```

---

### 2. 주문 수정 API

- 주문 고객의 주문을 수정합니다.
- 배송 상태(deliveryStatus)가 PENDING일 때만 주소 변경이 가능합니다.

### 요청 (Request)

- **URL**: `/api/order`
- **Method**: `PUT`
- **Content-Type**: `application/json`

### 요청 바디 (Request Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `orderId` | String | O | 주문 ID |
| `address` | String | X | 변경할 주소 |

### 요청 예시

```json
{
  "orderId": "000001",
  "address": "부산"
}
```

### 응답 (Response)

- **HTTP Status Code**: `200 OK`

### 응답 바디 (Response Body)

| 필드명 | 타입 | 필수 여부 | 설명 |
| --- | --- | --- | --- |
| `code` | int | O | 응답 코드 (0: 성공, 그 외: 실패) |
| `message` | String | O | 응답 메시지 |
| `data` | Object | X | 응답 데이터 |

### 응답 예시

```json
{
  "code": 0,
  "message": "성공",
  "data": null
}
```

---
