package ggs.srr.oauth.provider;

import ggs.srr.oauth.auth.AuthenticatedUser;
import ggs.srr.oauth.auth.AuthenticatedUserManager;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.TokenRequestDto;
import ggs.srr.oauth.provider.dto.OAuth2Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class Provider42 implements Provider{

    private AuthenticatedUserManager userManager;

    public Provider42(AuthenticatedUserManager userManager){
        this.userManager = userManager;
    }

    @Override
    public void authentication(HttpServletRequest request, Client client) {
        String code = request.getParameter("code");

        TokenRequestDto requestDto = getTokenRequestDto(client, code);
        RestTemplate restTemplate = new RestTemplate();
        OAuth2Token tokenResponseDto = restTemplate.postForObject(client.getTokenUri(), requestDto, OAuth2Token.class);
        AuthenticatedUser authenticatedUser = userManager.CreateAuthenticatedUser(tokenResponseDto, client);
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", authenticatedUser);
    }

    private static TokenRequestDto getTokenRequestDto(Client client, String code) {
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setGrant_type(client.getGrantType());
        requestDto.setClient_id(client.getClientId());
        requestDto.setClient_secret(client.getClientSecret());
        requestDto.setRedirect_uri(client.getRedirectURL());
        requestDto.setScope(client.getScope());
        requestDto.setCode(code);
        return requestDto;
    }


}
