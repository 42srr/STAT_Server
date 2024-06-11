package ggs.srr.service.oauth2.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2user implements OAuth2User {


    private UserDto userDto;

    public CustomOAuth2user(UserDto userDto) {
       this.userDto = userDto;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return userDto.getOAuth2User().getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDto.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userDto.getFtResponse()
                .getName();
    }
}
