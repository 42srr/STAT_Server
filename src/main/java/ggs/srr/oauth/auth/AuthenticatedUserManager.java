package ggs.srr.oauth.auth;

import ggs.srr.domain.user.FtUser;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.OAuth2Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;

@Slf4j
@Component
public class AuthenticatedUserManager {

    public AuthenticatedUser createAuthenticatedUser(OAuth2Token responseDto, Client client){

        String tokenType = responseDto.getToken_type();
        String accessToken = responseDto.getAccess_token();
        log.info("access token = {}", accessToken);

        HttpHeaders headers = createHeader(tokenType, accessToken);
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> responseEntity = restTemplate.exchange(client.getUserInfoUri(), HttpMethod.GET, request, HashMap.class);
        FtUserDataParser ftUserDataParser = new FtUserDataParser();
        FtUser ftUser = ftUserDataParser.parseUser(responseEntity);
        return new AuthenticatedUser(ftUser, client, responseDto);
    }

    private HttpHeaders createHeader(String tokenType, String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenType + " " + accessToken);
        return headers;
    }


}
