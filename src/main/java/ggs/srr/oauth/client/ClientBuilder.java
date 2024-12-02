package ggs.srr.oauth.client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientBuilder {

    private String name;
    private String grant_type;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private String authorizationUri;
    private String tokenUri;
    private String userInfoUri;
    private Set<String> scope = new HashSet<>();

    public ClientBuilder grantType(String grant_type){
        this.grant_type = grant_type;
        return this;
    }

    public ClientBuilder scope(String... scopes){
        for(String scope : scopes)
            this.scope.add(scope);
        return this;
    }

    public ClientBuilder name(String name){
        this.name = name;
        return this;
    }

    public ClientBuilder clientId(String clientId){
        this.clientId = clientId;
        return this;
    }

    public ClientBuilder clientSecret(String clientSecret){
        this.clientSecret = clientSecret;
        return this;
    }

    public ClientBuilder redirectURL(String redirectURL){
        this.redirectURL = redirectURL;
        return this;
    }

    public ClientBuilder authorizationUri(String authorizationUri){
        this.authorizationUri = authorizationUri;
        return this;
    }

    public ClientBuilder tokenUri(String tokenUri){
        this.tokenUri = tokenUri;
        return this;
    }

    public ClientBuilder userInfoUri(String userInfoUri){
        this.userInfoUri = userInfoUri;
        return this;
    }

    public Client build(){
        return new Client(this.grant_type, this.name, this.clientId, this.clientSecret, this.redirectURL,
                    this.authorizationUri, this.tokenUri, this.userInfoUri, this.scope);
    }
}
