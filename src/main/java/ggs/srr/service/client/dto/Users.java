package ggs.srr.service.client.dto;

import java.util.List;

public class Users {
    private final List<UserContent> userIds;

    public Users(List<UserContent> userIds) {
        this.userIds = userIds;
    }
}
