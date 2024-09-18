# 테이블 주문 플랫폼 서비스

이 플랫폼은 사용자가 메뉴 항목을 탐색하고, 카트에 추가하며, 카트를 관리하고, 주문을 쉽게 할 수 있는 테이블 주문 플랫폼입니다. 이 플랫폼은 Spring Boot로 개발되었으며, 여러 가지 의존성을 사용하여 안정적인 경험을 제공합니다. 또한, 데이터베이스로 MySQL을 사용하여 데이터를 저장하고 관리합니다. **기본 인증 보안(Basic Authentication Security)**을 구현하여, 사용자가 플랫폼을 처음 사용할 때 반드시 회원가입과 로그인을 해야 합니다.
## 주요 기능:
- 카트에 항목 추가
- 카트 항목 관리(업데이트 및 삭제)
- 주문 생성 및 관리
- 기본 인증을 통한 보안 로그인

---
## 사용된 Dependency

- **spring-boot-starter-web**: 웹과 REST API 기능을 제공하는 의존성입니다.
- **spring-boot-starter-security**: 인증과 권한 부여 기능을 추가해주는 의존성입니다.
- **spring-boot-starter-data-jpa**: JPA를 사용하여 데이터베이스 작업을 쉽게 할 수 있게 해주는 의존성입니다.
- **mysql-connector-j**: MySQL 데이터베이스와 연결해주는 드라이버입니다.
- **lombok**: 코드를 줄여주는 라이브러리로, getter, setter 등을 자동으로 생성해줍니다.
- **spring-boot-starter-test**: 단위 테스트와 통합 테스트를 지원하는 의존성입니다.
- **jakarta.persistence-api**: JPA 기능을 제공하는 자바의 표준 API입니다.
- **spring-security-test**: Spring Security 테스트를 지원하는 의존성입니다.


---

# API 문서

### POST `/auth/register`
- **설명**: 새로운 사용자를 등록합니다.
- **요청 바디**:
    ```json
    {
      "phoneNumber": "01012345678",
      "password": "password123",
      "role": "USER"
    }
    ```
  ![Project Logo](src/main/resources/static/images/register.png)
- **응답**:
    - 201 생성됨 (사용자가 성공적으로 등록됨)
    - 응답 예시:
      ```json
      {
        "message": "사용자가 성공적으로 등록되었습니다. 로그인해주세요."
      }
      ```

### POST `/auth/login`
- **설명**: 사용자가 로그인합니다.
- **요청 바디**:
    ```json
    {
      "phoneNumber": "01012345678",
      "password": "password123"
    }
    ```
  ![Project Logo](src/main/resources/static/images/login.png)
- **응답**:
    - 200 성공 (로그인 성공)
    - 응답 예시:
      ```json
      {
        "message": "로그인 성공!"
      }
      ```

### POST `/auth/logout`
- **설명**: 현재 사용자를 로그아웃합니다.
- **응답**:
    - 200 성공 (로그아웃 성공)
    - 응답 예시:
      ```json
      {
        "message": "로그아웃 성공!"
      }
      ```

---

## 카트 아이템 관리

### POST `/cart-items/carts/{cartId}`
- **설명**: `cartId`에 새 카트 아이템을 추가합니다.
- **요청 바디**:
    ```json
    {
      "id": 1,
      "menu": {
        "id": 1
      },
      "quantity": 2
    }
    ```
- **응답**:
    - 201 생성됨 (카트 아이템이 성공적으로 추가됨)
    - 응답 예시:
      ```json
      {
        "id": 1,
        "menu": {
          "id": 1,
          "name": "비빔밥",
          "price": 10.99,
          "description": "전통 한식 요리"
        },
        "quantity": 2
      }
      ```

### GET `/cart-items/{cartItemId}`
- **설명**: `cartItemId`로 카트 아이템을 가져옵니다.
- **응답**:
    - 200 성공 (카트 아이템이 성공적으로 조회됨)
    - 응답 예시:
      ```json
      {
        "id": 1,
        "menu": {
          "id": 1,
          "name": "비빔밥",
          "price": 10.99,
          "description": "전통 한식 요리"
        },
        "quantity": 2
      }
      ```

### GET `/cart-items/carts/{cartId}`
- **설명**: `cartId`에 있는 모든 카트 아이템을 가져옵니다.
- **응답**:
    - 200 성공 (카트 아이템 목록)
    - 응답 예시:
      ```json
      [
        {
          "id": 1,
          "menu": {
            "id": 1,
            "name": "비빔밥",
            "price": 10.99,
            "description": "전통 한식 요리"
          },
          "quantity": 2
        },
        {
          "id": 2,
          "menu": {
            "id": 2,
            "name": "김치",
            "price": 5.99,
            "description": "매운 발효 야채"
          },
          "quantity": 1
        }
      ]
      ```

### PUT `/cart-items/{cartItemId}`
- **설명**: `cartItemId`로 기존 카트 아이템을 수정합니다.
- **요청 바디**:
    ```json
    {
      "menu": {
        "id": 1
      },
      "quantity": 3
    }
