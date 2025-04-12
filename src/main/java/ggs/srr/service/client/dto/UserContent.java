package ggs.srr.service.client.dto;

import lombok.Getter;

@Getter
public class UserContent {
    private String intraId;
    private String serverId;

    public UserContent() {
    }

    public UserContent(String intraId, String serverId) {
        this.intraId = intraId;
        this.serverId = serverId;
    }
}
