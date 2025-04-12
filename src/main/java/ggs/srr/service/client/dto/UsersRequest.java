package ggs.srr.service.client.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UsersRequest {
    private List<UserContent> userContents;

    public UsersRequest() {
    }

    public UsersRequest(List<UserContent> userContents) {
        this.userContents = userContents;
    }

    public Users usersRequestToUsers() {
        return new Users(userContents);
    }
}
