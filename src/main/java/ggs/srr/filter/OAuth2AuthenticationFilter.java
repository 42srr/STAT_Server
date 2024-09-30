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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getRequestURL().toString();
        Client client = clientManager.getClient("42");
        String redirectURL = client.getRedirectURL();


        if (url.equals(redirectURL)) {
            authentication(request, client);
            response.sendRedirect("http://localhost:8080");
            return;
        }
        else {
            HttpSession session = request.getSession();
            if (session.getAttribute("42SessionId") == null) {
                log.info("null session id");
                response.sendRedirect(client.getAuthorizationUri());
                return;
            }
        }
        doFilter(request, response, filterChain);
    }

    private void authentication(HttpServletRequest request, Client client) {
        providerManager.attemptAuthentication(request, client);
    }
}
