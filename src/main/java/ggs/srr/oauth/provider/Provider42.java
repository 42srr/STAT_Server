package ggs.srr.oauth.provider;

import ggs.srr.domain.user.FtUser;
import ggs.srr.jwt.JWTUtil;
import ggs.srr.oauth.auth.AuthenticatedUser;
import ggs.srr.oauth.auth.AuthenticatedUserManager;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.JwtToken;
import ggs.srr.oauth.provider.dto.ft.TokenRequestDto;
import ggs.srr.oauth.provider.dto.OAuth2Token;
import ggs.srr.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static ggs.srr.jwt.JWTUtil.ACCESS_TOKEN_EXPIRE_MS;
import static ggs.srr.jwt.JWTUtil.REFRESH_TOKEN_EXPIRE_MS;

@Component
@Transactional(readOnly = true)
@Slf4j
public class Provider42 implements Provider{

    private AuthenticatedUserManager userManager;
    private JWTUtil jwtUtil;
    private UserService userService;

    public Provider42(AuthenticatedUserManager userManager, JWTUtil jwtUtil, UserService userService){
        this.userManager = userManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Transactional(readOnly = false)
    @Override
    public JwtToken authentication(String authorizationCode, Client client) {


        TokenRequestDto requestDto = getTokenRequestDto(client, authorizationCode);
        RestTemplate restTemplate = new RestTemplate();
        OAuth2Token tokenResponseDto = restTemplate.postForObject(client.getTokenUri(), requestDto, OAuth2Token.class);
        FtUser user = userManager.createFtUser(tokenResponseDto, client);

        String accessToken = jwtUtil.createJWT(user.getIntraId(), user.getRole(), ACCESS_TOKEN_EXPIRE_MS);
        String refreshToken = jwtUtil.createJWT(user.getIntraId(), user.getRole(), REFRESH_TOKEN_EXPIRE_MS);

        user.setJwtToken(accessToken, refreshToken);

        Optional<FtUser> findUserOptional = userService.findByIntraId(user.getIntraId());
        if (findUserOptional.isEmpty()) {
            userService.save(user);
        } else {
            FtUser findUser = findUserOptional.get();
            findUser.setJwtToken(accessToken, refreshToken);
            findUser.setOauth2Token(user.getOAuth2AccessToken(), user.getOauth2RefreshToken());
        }

        log.info("jwt access= {}", accessToken);
        log.info("jwt refresh = {}", refreshToken);
        return new JwtToken(accessToken, refreshToken);
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
