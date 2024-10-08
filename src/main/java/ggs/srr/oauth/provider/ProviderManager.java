package ggs.srr.oauth.provider;


import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientManager;
import ggs.srr.oauth.provider.dto.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderManager {

    private Provider provider;

    @Autowired
    public ProviderManager (Provider provider){
        this.provider = provider;
    }

    public JwtToken attemptAuthentication(String authorizationCode, Client client){
        return provider.authentication(authorizationCode, client);
    }
}
