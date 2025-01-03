package ggs.srr.jwt;

import ggs.srr.security.AuthenticationHolder;
import ggs.srr.security.authentication.Authentication;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final JWTExceptionHandler jwtExceptionHandler;
    private final AuthenticationHolder authenticationHolder;


    public JWTFilter(JWTUtil jwtUtil, JWTExceptionHandler jwtExceptionHandler,
                     AuthenticationHolder authenticationHolder) {
        this.jwtUtil = jwtUtil;
        this.jwtExceptionHandler = jwtExceptionHandler;
        this.authenticationHolder = authenticationHolder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("request uri = {}", requestURI);
        if (requestURI.startsWith("/login") || requestURI.startsWith("/refresh") || requestURI.startsWith("/swagger-ui")
                || requestURI.startsWith("/api-docs")) {
            doFilter(request, response, filterChain);
            return;
        }

        String authorization = request.getHeader("Authorization");
        System.out.println("authorization = " + authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("jwt null");
            return;
        }

        String token = authorization.split(" ")[1];
        try {
            jwtUtil.isExpired(token);
        } catch (JwtException e) {
            jwtExceptionHandler.handle(token, response, e);
            return;
        }

        String intraId = jwtUtil.getIntraId(token);
        String role = jwtUtil.getRole(token);

        authenticationHolder.setAuthentication(new Authentication(intraId, role));

        log.info("intraId = {}", authenticationHolder.getAuthentication().getIntraId());
        filterChain.doFilter(request, response);
        log.info("end!!!!");
        authenticationHolder.clearHolder();

    }


}
