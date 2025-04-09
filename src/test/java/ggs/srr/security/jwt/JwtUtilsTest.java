package ggs.srr.security.jwt;

import ggs.srr.domain.user.Role;
import ggs.srr.exception.security.authentication.AuthenticationException;
import ggs.srr.security.jwt.request.CreateJwtRequest;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    JwtUtils jwtUtils;

    @Value("${jwt.access_expire_ms}")
    private Long expireMs;

    @DisplayName("만료 기간이 지난 access token 검증시 예외가 발생한다.")
    @Test
    void validateExpiredAccessToken() {
        //given

        Long requestTimeMs = System.currentTimeMillis() - (expireMs);
        JwtTokenResponse jwtToken = jwtUtils.createJwtToken(new CreateJwtRequest("test", Role.CADET), requestTimeMs);

        //when //then
        assertThatThrownBy(() -> jwtUtils.validateToken(jwtToken.getAccessToken()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("만료된 JWT Token 입니다.");
    }

    @DisplayName("올바르지 않은 Access token 이 들어올 경우 예외를 발생시킨다.")
    @Test
    void invalidAccessToken() {
        //given
        String invalidAccessToken = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjEdwDQyMDkwODIsImozcyI6IlNSUiIsImludHJhSWQiOiJqb29qZW9uIiwicm9sZSI6ImNhcmRldCIsImV4cCI6MTc0NDIwOTA4Mn0.w01GdDpqnQebAgII8Hbyc5dkjhwKR2_jpxbKf9qjJQk";
        //when
        assertThatThrownBy(() -> jwtUtils.validateToken(invalidAccessToken))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("jwt token 검증시 오류가 발생했습니다.");
        //then
    }
}