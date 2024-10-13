package ggs.srr.jwt;

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

    public JWTFilter(JWTUtil jwtUtil, JWTExceptionHandler jwtExceptionHandler) {
        this.jwtUtil = jwtUtil;
        this.jwtExceptionHandler = jwtExceptionHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/login") || requestURI.startsWith("/refresh")) {
            doFilter(request, response, filterChain);
            return;
        }

        String authorization = request.getHeader("Authorization");
        System.out.println("authorization = " + authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("jwt null");
            return ;
        }

        String token = authorization.split(" ")[1];
        try {
            jwtUtil.isExpired(token);
        } catch (JwtException e) {
            jwtExceptionHandler.handle(token, response, e);
            return ;
        }

        String intraId = jwtUtil.getIntraId(token);
        String role = jwtUtil.getRole(token);

        log.info("intraId = {} role = {}", intraId, role);
        filterChain.doFilter(request, response);

    }
}
