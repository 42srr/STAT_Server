package ggs.srr.oauth.client;


import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Getter
public class Client {

    private String grantType;
    private String name;
    private String clientId;
    private String clientSecret;
    private String redirectURL;
    private String authorizationUri;
    private String userInfoUri;
    private String tokenUri;
    private List<String> scope;

    public Client(String grantType, String name, String clientId, String clientSecret, String redirectURL,
                  String authorizationUri, String tokenUri, String userInfoUri ,List<String> scope) {
        this.grantType = grantType;
        this.name = name;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectURL = redirectURL;
        this.authorizationUri = authorizationUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
        this.scope = scope;
    }


    public String getAuthorizationUri(){
        return this.authorizationUri + "?client_id=" + clientId + "&redirect_uri="+ getEncodedRedirectUri() +"&response_type=code";
    }



    private String getEncodedRedirectUri(){
        return URLEncoder.encode(this.redirectURL, StandardCharsets.UTF_8);
    }

}
