package ggs.srr.service.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.domain.user.FtUser;
import ggs.srr.service.system.dto.UserDto;
import ggs.srr.service.system.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.system.dto.UsersRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class APIClientImpl implements APIClient{

    private final String FETCHPROJECT_URL_BASE = "http://localhost:8081";
    private final String TURBOFETCH_URL_BASE = "http://localhost:8082";

    @Override
    public List<String> fetchProjectsFromFetchProject() {
        String url = FETCHPROJECT_URL_BASE + "/projects";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Object data = response.getBody().get("data");
            if (data instanceof List<?>) {
                return ((List<?>) data).stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching projects from FetchProject: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    @Override
    public List<UserDto> fetchUsersFromTurbofetch() {
        String url = TURBOFETCH_URL_BASE + "/users";
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            Object data = response.getBody().get("data");
            if (data instanceof List<?>) {
                ObjectMapper objectMapper = new ObjectMapper();
                return ((List<?>) data).stream()
                        .map(obj -> objectMapper.convertValue(obj, UserDto.class))
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching users from Turbofetch: {}", e.getResponseBodyAsString());
            throw e;
        }
    }


    @Override
    public List<UsersProjectsResponse> fetchUserProjectsFromFetchProject(UsersRequest usersRequest){
        String url = FETCHPROJECT_URL_BASE + "/user_projects";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsersRequest> httpEntity = new HttpEntity<>(usersRequest, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    httpEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            log.info("Response body: {}", response.getBody());

            Object data = response.getBody().get("data");
            if (data instanceof List<?> dataList) {
                ObjectMapper objectMapper = new ObjectMapper();
                return dataList.stream()
                        .map(obj -> objectMapper.convertValue(obj, UsersProjectsResponse.class))
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching user projects from FetchProject: {}", e.getResponseBodyAsString());
            throw e;
        }
    }
}
