package ggs.srr.oauth.provider.dto;

import lombok.Data;

@Data
public class OAuth2Token {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private long created_at;
    private long secret_valid_until;
}
