package ggs.srr.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.ApiResponse;
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

        ApiResponse<String> apiResponse = null;
        if (status.is4xxClientError()) {
            log.error("err: 4xx error");
            message = "올바르지 않은 Authorization Code 입니다. 다시 시도해 주세요";
            apiResponse = ApiResponse.unAuthorized(message);
        } else {
            log.error("err: 5xx error");
            e.printStackTrace();
            message = "42 Authorization Server 오류 입니다. 나중에 다시 시도해 주세요.";
            apiResponse = ApiResponse.internalServerError(message);
        }

        String content = objectMapper.writeValueAsString(apiResponse);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(content);
    }
}
