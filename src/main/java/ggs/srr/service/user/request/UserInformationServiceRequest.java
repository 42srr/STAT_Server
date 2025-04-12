package ggs.srr.service.user.request;

import lombok.Getter;

@Getter
public class UserInformationServiceRequest {

    private Long id;

    public UserInformationServiceRequest(Long id) {
        this.id = id;
    }
}
