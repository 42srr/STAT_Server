package ggs.srr.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.domain.user.Role;
import ggs.srr.security.authentication.response.AuthenticationResponse;
import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import ggs.srr.security.authentication.response.UserDetails;
import ggs.srr.security.authentication.client.UserDetailClient;
import ggs.srr.security.jwt.JwtUtils;
import ggs.srr.security.jwt.request.CreateJwtRequest;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import ggs.srr.service.user.UserDetailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandler {

    private final UserDetailClient userDetailClient;
    private final UserDetailService userDetailService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    public void handle(AuthorizationServerResponse authorizationServerResponse, HttpServletResponse response) throws IOException {
        UserDetails details = userDetailClient.getUserDetails(authorizationServerResponse);

        JwtTokenResponse jwtToken = jwtUtils.createJwtToken(new CreateJwtRequest(details.getIntraId(), Role.CADET));
        userDetailService.updateUserDetails(details, authorizationServerResponse, jwtToken);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .tokenType("bearer")
                .intraId(details.getIntraId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .expire(jwtToken.getExpire())
                .build();

        String content = objectMapper.writeValueAsString(ResponseEntity.ok(authenticationResponse));
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(content);
    }
}
