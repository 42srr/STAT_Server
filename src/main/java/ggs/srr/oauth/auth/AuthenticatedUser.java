package ggs.srr.oauth.auth;

import ggs.srr.domain.user.ft.FtUser;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.OAuth2Token;
import lombok.Getter;

@Getter
public class AuthenticatedUser {
    private FtUser user;
    private Client client;
    private OAuth2Token token;

    public AuthenticatedUser(FtUser user, Client client, OAuth2Token token) {
        this.user = user;
        this.client = client;
        this.token = token;
    }

}
