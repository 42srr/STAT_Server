package ggs.srr.security.authentication;

import lombok.Getter;

@Getter
public class Authentication {

    private final String intraId;
    private final String role;

    public Authentication(String intraId, String role) {
        this.intraId = intraId;
        this.role = role;
    }

}
