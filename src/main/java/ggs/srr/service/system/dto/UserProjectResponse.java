package ggs.srr.service.system.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class UserProjectResponse {
    @Setter
    @Getter
    public static class UsersProjectsResponse {
        String intraId;
        List<ParsingResponseDto> allProjectsResponse;
    }
}
