package ggs.srr.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.domain.user.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
class AuthorizationFilterTest {

    @DisplayName("관리자일 경우 admin 으로 시작하는 경로로 접근할 수 있다.")
    @Test
    void admin() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        request.setAttribute("intraId", "test");
        request.setAttribute("role", Role.ADMIN);
        request.setRequestURI("/admin/a/b/c");

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(new ObjectMapper());

        //when
        authorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("관리자가 아닌 사용자가 admin 으로 시작하느 경로로 접근시 403 응답이 반환된다.")
    @Test
    void forbidden() throws ServletException, IOException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);

        request.setAttribute("intraId", "test");
        request.setAttribute("role", Role.CADET);
        request.setRequestURI("/admin/a/b/c");

        AuthorizationFilter authorizationFilter = new AuthorizationFilter(new ObjectMapper());

        //when
        authorizationFilter.doFilterInternal(request, response, filterChain);

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

}