package ggs.srr.service.system;

import ggs.srr.service.system.dto.UserDto;
import ggs.srr.service.system.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.system.dto.UsersRequest;

import java.util.List;

public interface APIClient {
    List<UsersProjectsResponse> fetchUserProjectsFromFetchProject(UsersRequest usersRequest);
    List<String> fetchProjectsFromFetchProject();
    List<UserDto> fetchUsersFromTurbofetch(String code);
}
