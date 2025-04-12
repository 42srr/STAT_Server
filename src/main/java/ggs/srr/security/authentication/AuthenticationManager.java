package ggs.srr.security.authentication;

import ggs.srr.security.authentication.client.AuthenticationClient;
import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationManager {

    private final AuthenticationClient authenticationClient;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationSuccessHandler successHandler;

    public void authenticate(HttpServletRequest request, HttpServletResponse response, String code) throws IOException {

        try {
            AuthorizationServerResponse authorizationServerResponse = authenticationClient.authenticate(code);
            successHandler.handle(request, response, authorizationServerResponse);
        } catch (HttpStatusCodeException e) {
            failureHandler.handle(e, response);
        }
    }
}
