package ggs.srr.oauth.provider;

import ggs.srr.controller.login.LoginInfoDto;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.provider.dto.JwtToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Provider {

    LoginInfoDto authentication(String authorizationCode, Client client);
}
