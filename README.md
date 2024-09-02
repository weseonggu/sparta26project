# 배달 관리 시스템
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://capsule-render.vercel.app/api?type=wave&color=auto&height=300&section=header&text=배달관리시스템&fontSize=90" />

## :mortar_board: 목차
[1. 개요](#1-개요)

[2. 주요 기능](#2-주요기능)

[3. 문제 해결](#3-문제해결)

[4. ERD](#4-erd)

[5. API명세서](#5-API 명세서)


## 1. 개요
### :computer: 기술 스택
#### Platform
![Windows](https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white)
![IntelliJ](https://img.shields.io/badge/IntelliJ-0078D6.svg?style=for-the-badge&logo=intellijidea&logoColor=#000000)

#### SERVER
![apachetomcat](https://img.shields.io/badge/tomcat-0078D6.svg?style=for-the-badge&logo=apachetomcat&logoColor=yellow)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

#### RDBMS
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
<img src="https://img.shields.io/badge/JPA-0078D6?style=for-the-badge&logo=MyBatis&logoColor=white">


#### Application Development / Skills
![Java](https://img.shields.io/badge/Java-0078D6?style=for-the-badge&logo=openjdk&logoColor=white)
![Java](https://img.shields.io/badge/springboot-0078D6?style=for-the-badge&logo=springboot&logoColor=#)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)



 
 
<hr>

### :busts_in_silhouette: 팀원 소개
- 팀장 - 위성구 :walking:  [Github](https://github.com/weseonggu)  
- 팀원 - 이수정 :walking: [Github](https://github.com/Krystal-13)
- 팀원 - 김원기 :walking: [Github](https://github.com/TrendFollow)
<hr>

### :flags: 기본 규칙
- 계층구조 : controller, service, repository 
  - dto (`Respoonse%%Dto` `Request%%Dto`), entity
  - 커밋 규칙 : feat contents (소문자 한칸 띄어쓰기 내용)
    
- Comment
  - 클래스와 메소드가 어떤 역할을 하는지
  - 다중 if/for 등 로직이 복잡한 경우 / 특별한 제약이 필요한 경우
  - 변수의 이름만으로 설명이 부족한 경우
 
- 기타
  - Spring MVC
  - Spring Boot
  - SRP(단일 책임 원칙)을 준수하여 함수와 클래스가 하나의 책임만을 갖도록 설계
  - 함수나 클래스는 작고 명확하게 유지하면서 코드의 중복을 줄여 유지보수를 용이하도록 설계
  - 진행 상태확인 및 대략적인 소모차트 예상 , 간단한 스크럼 회의 이후 스프린트 진행, 스프린트 리뷰 등의 방법으로 지속적인 커뮤니케이션 유지
  - 혼자 해결하기 어려운 부분들은 팀원들과 소통을 통해 다각적인 해결 시도
 

## 2. 주요기능
:arrow_right: 위성구 
본인 역할 : 
  - JWT, Spring Security를 사용한 인증 인가 구현
  - Member, Address 도메인 테이블의 CRUD 구현
  - DDD(도메인 중심 설계): MSA로 리팩토링을 좀더 간단하게 하기위한 패키징 전략 사용
  - 레디스를 사용한 회원 정보 캐싱
  - AOP를 사용하여 데이터 등록 갱신요청에 대한 로그처리

:arrow_right: 이수정
본인 역할 : 
  - 주문, 결제 API 구현
  - MSA 전환 대비 인터페이스 적용
  - 온라인 및 오프라인 결제에 따른 커스텀 유효성 검사
  - 에러 처리 및 예외 핸들링
  - 페이징 및 필터링 기능 구현

:arrow_right: 김원기
본인 역할 : 
- RDS 데이터 베이스 연동

- Entity 연관관계 매핑

- querdsl 페이징 처리(정렬조건, 검색조건)

- category, deliveryzone, operatinghours, product, store, ai CRUD 구현

- aistudio ai를 RestTemplate 활용한 API 통신(상품 이름 추천 받기)

- aop 활용한 로그처리, 커스텀 예외 클래스를 활용한 예외 처리

## 3. 문제 해결
:arrow_right: 발생한 문제

- 권한 관리를 해서 처리를 하는것이 유지보수도 어렵고, 코드의 가독성도 떨어지는것 같음.

- 모놀리틱 > MSA 전환을 고려하여 설계를 하다보니 각 서비스를 호출하는 곳에서 순환 참조가 발생하였다.

:arrow_right: 개선 사항

- 권한마다 url 분리하여 처리하는 것도 하나의 좋은 대안이 될 수 있을것 같다고 생각하였다.

- 각 도메인별 관계가 깊은 것은 독립적인 애플리케이션으로 두지 않고 같이 묶어서 처리되도록 개선하였다.



## 4. Erd
![팀플 ERD](https://github.com/weseonggu/sparta26project/blob/master/ERD.png)

## 5. API 명세서
[API 명세서](https://imported-turner-0f5.notion.site/e498bae6ae144848bbbae6963eacabb4?v=6f312b3188ba42aeb9bc0e1b1344758d&pvs=4)











