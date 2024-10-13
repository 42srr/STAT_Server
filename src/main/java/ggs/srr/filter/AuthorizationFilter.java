package ggs.srr.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.jwt.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Autowired
    public AuthorizationFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/login") || requestURI.startsWith("/refresh") || requestURI.startsWith("/index")){
            filterChain.doFilter(request, response);
            return ;
        }

        String authorization = request.getHeader("Authorization");
        String token = authorization.split(" ")[1];
        String role = jwtUtil.getRole(token);

        if (requestURI.startsWith("/admin")) {
            if (!role.equals("ADMIN")) {
                log.info("authorization fail");
                handleUnAuthorizationUser(response);
                return ;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleUnAuthorizationUser(HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            HashMap<String, String> errorResult = new HashMap<>();
            errorResult.put("message", "authorization fail");

            String result = objectMapper.writeValueAsString(errorResult);
            response.getWriter().write(result);
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

        } catch (JsonProcessingException e) {
            log.info("json processing error");
        } catch (IOException e) {
            log.info("io exception");
        }
    }
}
