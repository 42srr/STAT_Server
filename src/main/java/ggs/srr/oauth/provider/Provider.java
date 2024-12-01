package ggs.srr.oauth.provider;

import ggs.srr.api.controller.login.LoginInfoDto;
import ggs.srr.oauth.client.Client;

public interface Provider {

    LoginInfoDto authentication(String authorizationCode, Client client);
}
