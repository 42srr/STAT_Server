package ggs.srr.oauth.provider;

import ggs.srr.oauth.client.Client;
import jakarta.servlet.http.HttpServletRequest;

public interface Provider {

    void authentication(HttpServletRequest request, Client client);
}
