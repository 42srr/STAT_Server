package ggs.srr.oauth.provider;


import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProviderManager {

    private Provider provider;

    @Autowired
    public ProviderManager (Provider provider, ClientManager clientManager){
        this.provider = provider;
    }

    public void attemptAuthentication(HttpServletRequest request, Client client){
        provider.authentication(request, client);
    }
}
