package ggs.srr.security.authentication.client;

import ggs.srr.exception.security.client.InvalidAuthenticationRequestException;
import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import ggs.srr.security.authentication.response.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class UserDetailClient {

    public UserDetails getUserDetails(AuthorizationServerResponse authorizationServerResponse) {
        RestTemplate template = new RestTemplate();

        HttpHeaders headers = getHeaders(authorizationServerResponse);
        HttpEntity<Void> request = new HttpEntity<>(headers);


        ResponseEntity<Map<String, Object>> response = template.exchange(
                "https://api.intra.42.fr/v2/me",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> content = response.getBody();

        if (content == null) {
            log.error("UserDetailClient.getUserDetails : 개인 정보를 불러오는데 실패했습니다.");
            throw new InvalidAuthenticationRequestException("사용자 개인 정보를 불러오는데 실패했습니다.");
        }

        content.keySet()
                .forEach(k -> log.debug("key = {}, value = {}", k, content.get(k)));

        return UserDetails.of(content);

    }

    private static HttpHeaders getHeaders(AuthorizationServerResponse authorizationServerResponse) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("Authorization", "Bearer " + authorizationServerResponse.getAccessToken());
        return new HttpHeaders(params);
    }

}
