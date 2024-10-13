java version : 21.0.4
spring boot version : 3.2.5

---

## API 명세

- http://118.67.134.143:8080/swagger-ui/index.html

## 2024.10.14 개발 로그

### update

- 기존 read me 에 있던 api 명세 swagger 를 통해 api page 로 제공
- 기존 projects/{intra_id} end point 반환값 생성

## issue

- Authorization grant 가 front 로 전달되는 구조로 변경
- Authorization grant 를 SRR 서버로 전달 시 자체 jwt token 발급 로직으로 변경
- 따라서 Redirect URI 를 다시 정해야함
- projects endpoint 응답시간 너무 오래 걸림
