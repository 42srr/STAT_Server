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

    public AuthorizationServerResponse authenticate(String code) {

        RestTemplate template = new RestTemplate();
        HttpEntity<Map<String, String>> request = getTokenRequestMessage(code);

        ResponseEntity<Map<String, Object>> response = template.exchange(
                "https://api.intra.42.fr/oauth/token",
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

        params.put("grant_type", "authorization_code");
        params.put("client_id", "u-s4t2ud-4a50cde47279b31ea6a0d216344bcdea8aa951ea9d57e3c11fa8fa265160753c");
        params.put("client_secret", "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");
        params.put("code", code);
        params.put("redirect_uri", "http://localhost:5173");

        return params;
    }

}
