# Coupon Service

## 1. API 엔드포인트

### 1.1 쿠폰 정책 API

#### 쿠폰 정책 생성
- Method: POST
- Path: `/api/v1/coupon-policies`
- Description: 새로운 쿠폰 정책을 생성합니다.
```json
{
    "title": "여름 할인 쿠폰",
    "description": "여름 시즌 특별 할인",
    "totalQuantity": 1000,
    "startTime": "2023-07-01T00:00:00",
    "endTime": "2023-07-31T23:59:59",
    "discountType": "FIXED_AMOUNT",
    "discountValue": 5000,
    "minimumOrderAmount": 50000,
    "maximumDiscountAmount": 5000
}
```

#### 쿠폰 정책 조회
- Method: GET
- Path: `/api/v1/coupon-policies/{id}`
- Description: 특정 쿠폰 정책의 상세 정보를 조회합니다.

#### 쿠폰 정책 목록 조회
- Method: GET
- Path: `/api/v1/coupon-policies`
- Description: 쿠폰 정책 목록을 조회합니다.
- Parameters:
    - page (optional): 페이지 번호 (default: 0)
    - size (optional): 페이지 크기 (default: 10)

### 1.2 쿠폰 API

#### 쿠폰 발행
- Method: POST
- Path: `/api/v1/coupons/issue`
- Description: 사용자에게 쿠폰을 발행합니다.
```json
{
    "couponPolicyId": 1,
    "userId": 100
}
```

#### 쿠폰 사용
- Method: POST
- Path: `/api/v1/coupons/{id}/use`
- Description: 쿠폰을 사용 처리합니다.
```json
{
    "orderId": 1000,
    "orderAmount": 100000
}
```

#### 쿠폰 취소
- Method: POST
- Path: `/api/v1/coupons/{id}/cancel`
- Description: 사용된 쿠폰을 취소 처리합니다.

#### 사용자 쿠폰 목록 조회
- Method: GET
- Path: `/api/v1/coupons/user/{userId}`
- Description: 특정 사용자의 쿠폰 목록을 조회합니다.
- Parameters:
    - status (optional): 쿠폰 상태 필터
    - page (optional): 페이지 번호 (default: 0)
    - size (optional): 페이지 크기 (default: 10)

## 2. 요청/응답 모델

### 2.1 쿠폰 정책

#### CouponPolicyRequest
```json
{
    "title": "String",
    "description": "String",
    "totalQuantity": "Integer",
    "startTime": "LocalDateTime",
    "endTime": "LocalDateTime",
    "discountType": "FIXED_AMOUNT | PERCENTAGE",
    "discountValue": "Integer",
    "minimumOrderAmount": "Integer",
    "maximumDiscountAmount": "Integer"
}
```

#### CouponPolicyResponse
```json
{
    "id": "Long",
    "title": "String",
    "description": "String",
    "totalQuantity": "Integer",
    "issuedQuantity": "Integer",
    "startTime": "LocalDateTime",
    "endTime": "LocalDateTime",
    "discountType": "FIXED_AMOUNT | PERCENTAGE",
    "discountValue": "Integer",
    "minimumOrderAmount": "Integer",
    "maximumDiscountAmount": "Integer"
}
```

### 2.2 쿠폰

#### CouponIssueRequest
```json
{
    "couponPolicyId": "Long",
    "userId": "Long"
}
```

#### CouponUseRequest
```json
{
    "orderId": "Long",
    "orderAmount": "Integer"
}
```

#### CouponResponse
```json
{
    "id": "Long",
    "couponPolicy": {
        "id": "Long",
        "title": "String",
        "discountType": "FIXED_AMOUNT | PERCENTAGE",
        "discountValue": "Integer",
        "minimumOrderAmount": "Integer",
        "maximumDiscountAmount": "Integer"
    },
    "userId": "Long",
    "orderId": "Long",
    "status": "AVAILABLE | USED | EXPIRED | CANCELED",
    "expirationTime": "LocalDateTime",
    "issuedAt": "LocalDateTime"
}
```

## 3. 에러 코드

### 3.1 공통 에러
- 400 Bad Request: 잘못된 요청
- 401 Unauthorized: 인증 실패
- 403 Forbidden: 권한 없음
- 404 Not Found: 리소스를 찾을 수 없음
- 500 Internal Server Error: 서버 내부 오류

### 3.2 비즈니스 에러
- 쿠폰 정책을 찾을 수 없음
- 쿠폰을 찾을 수 없음
- 이미 사용된 쿠폰
- 만료된 쿠폰
- 취소된 쿠폰
- 쿠폰 수량 초과
- 유효하지 않은 쿠폰 기간

## 4. 보안

### 4.1 인증
- JWT 토큰 기반 인증
- Authorization 헤더에 Bearer 토큰 포함

## 5. API 버전 관리
- URI 버전 관리 (/api/v1/...)
- 하위 호환성 유지
- 주요 변경 시 새로운 버전 생성