package ggs.srr.filter;

import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientManager;
import ggs.srr.oauth.provider.ProviderManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class OAuth2AuthenticationFilter extends OncePerRequestFilter {

    private ClientManager clientManager;
    private ProviderManager providerManager;


    public OAuth2AuthenticationFilter(ClientManager clientManager, ProviderManager providerManager) {
        this.clientManager = clientManager;
        this.providerManager = providerManager;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        System.out.println(requestURL);
        Client client = clientManager.getClient("42");
        if (requestURL.equals(client.getRedirectURL())){
            log.info("redirect uri = {}" , client.getRedirectURL());
            authentication(request, client);
            response.sendRedirect("http://localhost:8080");
            return ;
        }
        HttpSession session = request.getSession(false);
        if (session == null){
            log.info("session is null");
            response.sendRedirect(client.getAuthorizationUri());
            return;
        }
        log.info("session = {}", session.getAttribute("42SessionId"));
        doFilter(request, response, filterChain);
    }

    private void authentication(HttpServletRequest request, Client client) {
        providerManager.attemptAuthentication(request, client);
    }

}
