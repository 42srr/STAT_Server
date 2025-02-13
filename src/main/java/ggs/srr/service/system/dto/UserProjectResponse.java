package ggs.srr.service.system.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class UserProjectResponse {
    @Setter
    @Getter
    public static class UsersProjectsResponse {
        String intraId;
        List<ParsingResponseDto> allProjectsResponse;
    }
}
