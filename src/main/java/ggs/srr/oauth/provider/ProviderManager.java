package ggs.srr.oauth.provider;


import ggs.srr.filter.dto.LoginInfoDto;
import ggs.srr.oauth.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderManager {

    private Provider provider;

    @Autowired
    public ProviderManager (Provider provider){
        this.provider = provider;
    }

    public LoginInfoDto attemptAuthentication(String authorizationCode, Client client){
        return provider.authentication(authorizationCode, client);
    }
}
