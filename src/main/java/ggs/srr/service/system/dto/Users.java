package ggs.srr.service.system.dto;

import java.util.List;

public class Users {
    private final List<UserContent> userIds;

    public Users(List<UserContent> userIds) {
        this.userIds = userIds;
    }
}
