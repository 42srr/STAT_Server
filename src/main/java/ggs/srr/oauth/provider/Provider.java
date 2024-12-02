package ggs.srr.oauth.provider;

import ggs.srr.filter.dto.LoginInfoDto;
import ggs.srr.oauth.client.Client;

public interface Provider {

    LoginInfoDto authentication(String authorizationCode, Client client);
}
