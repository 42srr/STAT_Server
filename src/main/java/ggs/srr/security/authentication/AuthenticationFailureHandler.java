package ggs.srr.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public void handle(HttpStatusCodeException e, HttpServletResponse response) throws IOException {
        HttpStatusCode status = e.getStatusCode();

        String message = e.getMessage();

        log.error("message = {}", message);

        if (status.is4xxClientError()) {
            log.error("err: 4xx error");
            message = "Error: Invalid Authorization Code. Please retry";
        } else if (status.is5xxServerError()){
            log.error("err: 5xx error");
            message = "Error: 42 Authorization Server error. Please retry later";
        }

        ResponseEntity<String> responseEntity = ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);

        String content = objectMapper.writeValueAsString(responseEntity);
        response.setStatus(status.value());
        response.getWriter().write(content);
    }
}
