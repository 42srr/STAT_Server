package ggs.srr.oauth.provider;

import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.TokenRequestDto;
import ggs.srr.oauth.provider.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.UUID;

@Component
@Slf4j
public class Provider42 implements Provider{

    @Override
    public void authentication(HttpServletRequest request, Client client) {
        String code = request.getParameter("code");

        TokenRequestDto requestDto = getTokenRequestDto(client, code);
        RestTemplate restTemplate = new RestTemplate();
        TokenResponseDto tokenResponseDto = restTemplate.postForObject(client.getTokenUri(), requestDto, TokenResponseDto.class);
        HttpSession session = request.getSession();
        session.setAttribute("42SessionId", UUID.randomUUID().toString());
        log.info("{}", tokenResponseDto);
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
