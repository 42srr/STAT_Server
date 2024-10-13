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

    public FtUser createFtUser(OAuth2Token responseDto, Client client){

        String tokenType = responseDto.getToken_type();
        String oAuth2AccessToken = responseDto.getAccess_token();
        String oAuth2RefreshToken = responseDto.getRefresh_token();
        log.info("access token = {}", oAuth2AccessToken);
        log.info("refresh token = {}", oAuth2RefreshToken);

        HttpHeaders headers = createHeader(tokenType, oAuth2AccessToken);
        HttpEntity request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HashMap> responseEntity = restTemplate.exchange(client.getUserInfoUri(), HttpMethod.GET, request, HashMap.class);

        FtUserDataParser ftUserDataParser = new FtUserDataParser();
        FtUser user= ftUserDataParser.parseUser(responseEntity);
        user.setOauth2Token(oAuth2AccessToken, oAuth2RefreshToken);
        return user;
    }

    private HttpHeaders createHeader(String tokenType, String accessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", tokenType + " " + accessToken);
        return headers;
    }


}
