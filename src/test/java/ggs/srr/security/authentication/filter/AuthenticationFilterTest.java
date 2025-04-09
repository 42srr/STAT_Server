package ggs.srr.security.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.exception.security.authentication.AuthenticationErrorCode;
import ggs.srr.exception.security.authentication.AuthenticationException;
import ggs.srr.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class AuthenticationFilterTest {

    @DisplayName("올바른 access token 이 들어올 경우 통과된다.")
    @Test
    void validAccessToken() throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);
        String validAccessToken = "this_is_valid_access_token";

        doNothing().when(jwtUtils)
                .validateToken(validAccessToken);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + validAccessToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        //when
        authFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(jwtUtils, times(1)).validateToken(validAccessToken);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @DisplayName("만 료된 access token 을 받을 경우 401 응답을 반환한다.")
    @Test
    void expiredAccessToken() throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        String expiredAccessToken = "this_is_invalid_access_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + expiredAccessToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        //when
        doThrow(new AuthenticationException(AuthenticationErrorCode.EXPIRED_JWT_ERR))
                .when(jwtUtils).validateToken(expiredAccessToken);

        authFilter.doFilterInternal(request, response, filterChain);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("올바르지 않은 access token 을 받을 경우 401 응받을 반환한다.")
    @Test
    void invalidAccessToken() throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        String invalidAccessToken = "this_is_invalid_access_token";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + invalidAccessToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        //when
        doThrow(new AuthenticationException(AuthenticationErrorCode.JWT_ERR))
                .when(jwtUtils).validateToken(invalidAccessToken);

        authFilter.doFilterInternal(request, response, filterChain);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("올바르지 않은 형식의 인증 헤더형식으로 들어올 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"qwd, qwd", "Authorization, qwdq", "Authorization, Bearer"})
    void invalidHeaderFormat(String key, String value) throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(key, value);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        authFilter.doFilterInternal(request, response, filterChain);

        //when //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Authorization header 가 없을 경우 401응 을 반환한다.")
    @Test
    void absenceAuthorizationHeader() throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        authFilter.doFilterInternal(request, response, filterChain);

        //when //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Authorization header 의 값이 Bearer 로 시작하지 않을 경우 401 응답이 반환된다.")
    @Test
    void notStartWithBearer() throws ServletException, IOException {
        //given

        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "not_start_with_Bearer");
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        authFilter.doFilterInternal(request, response, filterChain);

        //when //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Bearer 뒤에 공백 후 값 이 없거나 Bearer 뒤에 바로 값이 붙어 있을 경우 401 응답이 반환된다.")
    @ParameterizedTest
    @ValueSource(strings = {"Bearer ", "Bearerwwwwwww"})
    void invalidBearerTokenFormat(String bearerToken) throws ServletException, IOException {

        //given
        JwtUtils jwtUtils = mock(JwtUtils.class);
        FilterChain filterChain = mock(FilterChain.class);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", bearerToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationFilter authFilter = new AuthenticationFilter(jwtUtils, new ObjectMapper());

        authFilter.doFilterInternal(request, response, filterChain);

        //when //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}