# SGA Spring Project

조직 내 공통 컴포넌트 검증/관리 목적의 **백엔드(Spring Boot) API 서버** 프로젝트입니다.  
`vue-project`와 연동하여 프론트 단독 검증을 넘어 API 통합 검증까지 수행합니다.

## 프로젝트 연계 구성

- Frontend (Vue): `https://github.com/used-comp-koboolean/vue-project`
- Backend (Spring): `https://github.com/used-comp-koboolean/spring-project`

## 운영 목적

- 사내 서비스에서 사용하는 컴포넌트를 표준화하고 재사용성을 높임
- 프론트-백엔드 연동 기반의 통합 검증 환경 확보
- 기능 추가 이력 및 버전 기준을 프로젝트 단위로 명확히 관리

## 권장 사용 순서

1. `spring-project` 실행 후 API 서버 기동
2. `vue-project`에서 컴포넌트 화면 및 API 연동 시나리오 검증
3. 변경 사항 발생 시 각 저장소 README의 버전/기능 목록 동기화

## 실행 방법

### 1) 사전 요구사항

- JDK 17 이상
- Gradle Wrapper 사용 가능 환경

### 2) 애플리케이션 실행

```bash
./gradlew bootRun
```

또는 IDE에서 Spring Boot 애플리케이션 메인 클래스를 실행합니다.

### 3) 빌드

```bash
./gradlew clean build
```

## API/환경 설정 안내

- 환경별 설정 값은 `src/main/resources` 하위 설정 파일에서 관리
- 업로드 파일 등 런타임 리소스는 `uploads/` 디렉터리를 사용
- 프론트 연동 시 CORS/포트 설정을 실행 환경에 맞게 조정

## 버전 관리 원칙

- 핵심 라이브러리 버전 변경 시 README에 즉시 반영
- API 스펙 또는 동작 변경 시 변경 이력을 기능 목록에 누적 기록
- 프론트(`vue-project`)와 연동되는 인터페이스 변경 시 양쪽 README 동기화

## 추가 기능 목록

- (예시) 공통 컴포넌트 검증용 API 엔드포인트 추가
- (예시) 컴포넌트 샘플 데이터 조회/등록 기능
- (예시) 파일 업로드/다운로드 API 기능

> 실제 기능이 추가될 때마다 위 목록에 항목을 누적해 관리하세요.
