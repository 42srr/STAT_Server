package ggs.srr.security.authentication.client;

import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class AuthenticationClient {

    @Value("${security.oauth2.client.42.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.42.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.client.42.grant-type}")
    private String grantType;

    @Value("${security.oauth2.client.42.redirect-uri}")
    private String redirectUri;

    @Value("${security.oauth2.client.42.token-uri}")
    private String tokenUri;

    public AuthorizationServerResponse authenticate(String code) {

        RestTemplate template = new RestTemplate();
        HttpEntity<Map<String, String>> request = getTokenRequestMessage(code);

        ResponseEntity<Map<String, Object>> response = template.exchange(
                tokenUri,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> content = response.getBody();

        log.debug("content = {}", content);
        log.debug("AuthenticationClient.authenticate: authentication success");

        return new AuthorizationServerResponse(content.get("access_token").toString(), content.get("refresh_token").toString());
    }

    private HttpEntity<Map<String, String>> getTokenRequestMessage(String code) {
        Map<String, String> params = createParameter(code);

        return new HttpEntity<>(params);
    }

    private Map<String, String> createParameter(String code) {
        Map<String, String> params = new HashMap<>();

        params.put("grant_type", grantType);
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("code", code);
        params.put("redirect_uri", redirectUri);

        return params;
    }

}
