package ggs.srr.service.oauth2;

import ggs.srr.service.oauth2.dto.CustomOAuth2user;
import ggs.srr.service.oauth2.dto.FtResponse;
import ggs.srr.service.oauth2.dto.UserDto;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2userService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if(registrationId.equals("42")){
            FtResponse ftResponse = new FtResponse(oAuth2User.getAttributes());
            UserDto userDto = new UserDto(ftResponse, oAuth2User, "ROLE_USER");
            return new CustomOAuth2user(userDto);
        }
        else
            return null;
    }
}
