package ggs.srr.oauth.provider.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TokenRequestDto {
    private String grant_type;
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private List<String> scopes = new ArrayList<>();

    public void setScope(List<String> scopes){
        for (String scope : scopes) {
            this.scopes.add(scope);
        }
    }
}
