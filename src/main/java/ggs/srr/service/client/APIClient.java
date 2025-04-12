package ggs.srr.service.client;

import ggs.srr.service.client.dto.UserDto;
import ggs.srr.service.client.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.client.dto.UsersRequest;

import java.util.List;

public interface APIClient {
    List<UsersProjectsResponse> fetchUserProjectsFromFetchProject(UsersRequest usersRequest);
    List<String> fetchProjectsFromFetchProject();
    List<UserDto> fetchUsersFromTurbofetch(String code);
}
