package ggs.srr.oauth.provider.dto.ft;

import java.util.HashSet;
import java.util.Set;
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
    private Set<String> scopes = new HashSet<>();

    public void setScope(Set<String> scopes){
        for (String scope : scopes) {
            this.scopes.add(scope);
        }
    }
}
