package ggs.srr.security.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.security.authentication.AuthenticationManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("login request");

        String code = request.getParameter("code");
        if (code == null) {
            log.error("Login Filter : Authorization code 누락");
            ResponseEntity<String> responseEntity = ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Error: Missing authorization code. Please attempt the request again.");

            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String content = objectMapper.writeValueAsString(responseEntity);
            response.getWriter().write(content);

            return ;
        }

        authenticationManager.authenticate(code, response);
    }
}
