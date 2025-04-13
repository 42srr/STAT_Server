# 42 SRR API

## 프로젝트 소개

- 42 본과정 활동에 관련된 정보를 가공하여 제공합니다.
- 개발기간 : 2024.04 ~

## 배포 정보

- API 문서: [42 SRR API](https://api.42srr.com/swagger-ui/index.html)
- 버전 : [![version](https://img.shields.io/badge/version-1.0.0-blue.svg)]() 
## 기술 스택

- jdk 17
- Spring boot 3.2.5
- Spring MVC
- JPA / Hibernate
- MySQL, H2

## 기능  

### 🔑 Login 

OAuth2 기반 **로그인 기능**을 제공합니다. 

- 42 OAuth2 인증 성공시 JWT Token 을 제공합니다. 해당 access token 으로 보호된 자원에 접근할 수 있습니다.

### 👥 User

**사용자의 정보**를 제공하는 기능 입니다. 

- 특정 사용자의 인트라 아이디, 레벨, 알타리안 달러, 보유 평가 포인트, 이미지 등을 제공합니다. 
- 사용자 레벨 분포를 제공합니다.
- 사용자의 레벨, 알타리안 달러, 보유 평가 포인트에 따른 랭킹을 제공합니다.
- 사용자가 완료하거나 진행중인 과제들의 정보를 제공합니다.
- 사용자의 특정 과제에 대한 과제 이름, 점수, 상태를 제공합니다.

### 📚 Project

**프로젝트에 대한 정보**를 제공합니다. 

- 프로젝트별 사용자 분포를 제공합니다.