## 📚 SDP BeanSpot Backend
빈스팟(Beanspot)은 분산된 환경 관련 공고들을 한 곳에 모아 제공하는 공고 큐레이션 플랫폼입니다.

## 👨‍💻 Developers
| <img src="https://avatars.githubusercontent.com/u/207793337?v=4" width=100> | <img src="https://avatars.githubusercontent.com/u/142705709?v=4" width=100> | <img src="https://avatars.githubusercontent.com/u/55704294?v=4" width=100> |
| :------------------------------------------------------------------------: | :-------------------------------------------------------------------------: | :-------------------------------------------------------------------------: | 
|                   [송현빈](https://github.com/hyunbin7664)                  |                     [장주은](https://github.com/jangjueun0429)                     |                    [하늘새미](https://github.com/haneulsaemi)                     |     

## 💻 기술 스택
- **Backend**: Java 17, Spring Boot 3.5.5, Spring Data JPA, Spring Data Elasticsearch

- **Infrastructure**: MySQL 8.0, Elasticsearch 8.x, AWS S3, AWS ECR/EC2

- **DevOps**: GitHub Actions, Docker, Docker Compose


## 🐳 Getting Started (Local Development)
Docker를 통해 데이터베이스(MySQL)와 검색 엔진(Elasticsearch)을 표준화된 환경에서 실행합니다.

1. **Docker Desktop 설치**
    - Windows / macOS: [Docker Desktop 공식 다운로드](https://docs.docker.com/desktop/setup/install/mac-install/)

2. **환경 변수 설정**
   <br>프로젝트 실행을 위해 노션 환경변수 페이지를 참고하여 루트 디렉토리에 .env 파일을 생성합니다.
```
# Database

# JPA & Flyway

# Elasticsearch

# AWS S3

# JWT & OAuth

```

3. **컨테이너 실행**
   <br>Docker Desktop을 실행한 상태에서 터미널을 열고 아래 명령어를 입력합니다.
```
# 1. 도커 컨테이너 백그라운드 실행
docker-compose up -d

# 2. 실행 상태 확인 (mysql-db, elasticsearch가 Up 인지 확인)
docker ps
```

## 📂 프로젝트 구조

```
├── src/main/java/com/beanspot/backend/
│   ├── config/                  # 외부 인프라 및 앱 전역 설정
│   ├── controller/              # API 엔드포인트
│   ├──  domain/                 # 엔티티 및 Enum
│   ├── dto/                     # 데이터 전송 객체 (Request, Response)
│   ├── repository/              # JPA(MySQL) 및 Search(Elasticsearch) 레포지토리
│   ├── security/                # JWT 인증 필터 및 권한 관리
│   ├── service/                 # 비즈니스 로직
├── .env                         # DB 및 AWS 인증 정보
├── docker-compose.yml           # 인프라(MySQL, ES) 실행 설정 파일
├── build.gradle                 # 의존성 및 빌드 설정
```