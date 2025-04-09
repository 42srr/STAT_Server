package ggs.srr.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.domain.user.Role;
import ggs.srr.security.authentication.client.UserDetailClient;
import ggs.srr.security.authentication.response.AuthenticationResponse;
import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import ggs.srr.security.authentication.response.UserDetails;
import ggs.srr.security.jwt.JwtUtils;
import ggs.srr.security.jwt.request.CreateJwtRequest;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import ggs.srr.service.user.UserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class AuthenticationSuccessHandler {

    private final UserDetailClient userDetailClient;
    private final UserDetailService userDetailService;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Value("${admin}")
    private List<String> adminIntraIdList;

    public AuthenticationSuccessHandler(UserDetailClient userDetailClient, UserDetailService userDetailService, JwtUtils jwtUtils, ObjectMapper objectMapper) {
        this.userDetailClient = userDetailClient;
        this.userDetailService = userDetailService;
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AuthorizationServerResponse authorizationServerResponse) throws IOException {
        UserDetails details = userDetailClient.getUserDetails(authorizationServerResponse);

        Role role = getRole(details);
        log.debug("admins = {}", adminIntraIdList);
        log.debug("user role = {}", role);
        setIntraIdAndRole(request, details, role);

        Long nowMs = System.currentTimeMillis();
        JwtTokenResponse jwtToken = jwtUtils.createJwtToken(new CreateJwtRequest(details.getIntraId(), role), nowMs);
        userDetailService.updateUserDetails(details, authorizationServerResponse, jwtToken, role);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .tokenType("Bearer")
                .intraId(details.getIntraId())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .expire(jwtToken.getExpire())
                .build();

        String content = objectMapper.writeValueAsString(ResponseEntity.ok(authenticationResponse));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write(content);
    }

    private Role getRole(UserDetails userDetails) {
        String intraId = userDetails.getIntraId();

        return adminIntraIdList.contains(intraId) ? Role.ADMIN : Role.CADET;
    }

    private void setIntraIdAndRole(HttpServletRequest request, UserDetails details, Role role) {
        String intraId = details.getIntraId();
        request.setAttribute("intraId", intraId);
        request.setAttribute("role", role);
    }
}
