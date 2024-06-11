package ggs.srr.service.oauth2.dto;

import java.util.Map;
import java.util.Objects;

public class FtResponse implements OAuth2Response{

    private final Map<String, Object> attributes;
    public FtResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "42";
    }

    @Override
    public String getProviderId() {
        return getProvider();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getName() {
        return attributes.get("usual_full_name").toString();
    }

    public String getUserId(){
        return attributes.get("id").toString();
    }

}
