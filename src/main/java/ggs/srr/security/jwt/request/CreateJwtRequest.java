package ggs.srr.security.jwt.request;

import ggs.srr.domain.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
public class CreateJwtRequest {

    private String intraId;
    private Role role;

    public CreateJwtRequest(String intraId, Role role) {
        this.intraId = intraId;
        this.role = role;
    }
}
