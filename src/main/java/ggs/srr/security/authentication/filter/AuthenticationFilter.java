package ggs.srr.security.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.exception.security.authentication.AuthenticationException;
import ggs.srr.security.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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

        try {
            String accessToken = getAccessToken(request);
            jwtUtils.validateToken(accessToken);
            jwtUtils.setAttribute(request, accessToken);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {

            ResponseEntity<String> responseEntity =  new ResponseEntity<>(
                    e.getMessage(),
                    UNAUTHORIZED
            );

            response.setStatus(UNAUTHORIZED.value());
            response.getWriter().write(objectMapper.writeValueAsString(responseEntity));
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
