package ggs.srr.service.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserProjectResponse {
    @Setter
    @Getter
    public static class UsersProjectsResponse {
        String intraId;
        List<ProjectDetailInfo> allProjectsResponse;
    }
}
