package ggs.srr.exception.security.client;

import org.springframework.web.client.RestClientException;

public class InvalidAuthenticationRequestException extends RestClientException {
    public InvalidAuthenticationRequestException(String message) {
        super(message);
    }
}
