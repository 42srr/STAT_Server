package ggs.srr.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
public class JWTExceptionHandler {

    ObjectMapper objectMapper = new ObjectMapper();

    public void handle(String token, HttpServletResponse response, Exception e) {
        String result = "";
        try {
            if (e instanceof ExpiredJwtException) {
                result = getResult("token is expired");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
            } else if (e instanceof JwtException) {
                result = getResult("invalid request");
                response.setStatus(HttpStatus.BAD_REQUEST.value());
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(result);
        } catch (JsonProcessingException ex) {
            log.error("object mapper error ", ex);
        } catch (IOException ex) {
            log.error("response message writer error ", ex);
        }
    }

    private String getResult(String message) throws JsonProcessingException {
        HashMap<String, String> errorResult = new HashMap<>();
        errorResult.put("message", message);
        return objectMapper.writeValueAsString(errorResult);
    }
}
