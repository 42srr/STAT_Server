package ggs.srr.api.controller.user.dto;

import java.util.List;
import lombok.Data;

@Data
public class UsersResponse {

    private final int size;
    private final List<UserResponse> users;

    public UsersResponse(List<UserResponse> users) {
        this.size = users.size();
        this.users = users;
    }
}
