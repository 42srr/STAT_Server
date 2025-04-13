package ggs.srr.security.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.ApiResponse;
import ggs.srr.exception.security.authentication.AuthenticationException;
import ggs.srr.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static ggs.srr.exception.security.authentication.AuthenticationErrorCode.INVALID_TOKEN_FORMAT_ERR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.debug("AuthenticationFilter.doFilterInternal");
        List<String> whiteList = List.of("/swagger", "/api-docs");

        String requestUri = request.getRequestURI();

        for (String uri : whiteList) {
            if (requestUri.startsWith(uri)) {
                filterChain.doFilter(request, response);
                return ;
            }
        }

        try {
            String accessToken = getAccessToken(request);
            jwtUtils.validateToken(accessToken);
            jwtUtils.setAttribute(request, accessToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {

            ApiResponse<String> apiResponse = ApiResponse.unAuthorized(e.getMessage());

            response.setStatus(UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
            response.setContentType("application/json");
        }
    }

    private String getAccessToken(HttpServletRequest request) throws AuthenticationException {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer")) {
            throw new AuthenticationException(INVALID_TOKEN_FORMAT_ERR);
        }

        String[] tokens = bearerToken.split(" ");
        if (tokens.length != 2) {
            throw new AuthenticationException(INVALID_TOKEN_FORMAT_ERR);
        }

        return tokens[1];
    }

}
