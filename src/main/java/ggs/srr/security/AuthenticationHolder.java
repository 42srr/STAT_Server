package ggs.srr.security;

import ggs.srr.security.authentication.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHolder {

    private ThreadLocal<Authentication> authenticationHolder = new ThreadLocal<>();

    public Authentication getAuthentication() {
        return authenticationHolder.get();
    }

    public void setAuthentication(Authentication authentication) {
        authenticationHolder.set(authentication);
    }

    public void clearHolder() {
        authenticationHolder.remove();
    }
}
