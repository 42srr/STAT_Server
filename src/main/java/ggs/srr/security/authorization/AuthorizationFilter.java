package ggs.srr.security.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.ApiResponse;
import ggs.srr.domain.user.Role;
import ggs.srr.exception.security.authorization.AuthorizationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static ggs.srr.exception.security.authorization.AuthorizationErrorCode.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            validateAuthorization(request);
            filterChain.doFilter(request, response);
        } catch (AuthorizationException e) {
            String message = e.getMessage();

            ApiResponse<String> apiResponse = ApiResponse.forbidden(message);
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            response.setStatus(FORBIDDEN.value());
            response.setContentType("application/json");

        }

    }

    private void validateAuthorization(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String intraId = request.getAttribute("intraId").toString();
        Role role = (Role) request.getAttribute("role");

        if (uri.startsWith("/admin") && !Role.isAdmin(role)) {
            log.error("AuthorizationFilter.validateAuthorization: 권한이 없는 사용자가 접근을 시도합니다. intraId = {}, role = {}", intraId, role);
            throw new AuthorizationException(UNAUTHORIZED);
        }
    }
}
