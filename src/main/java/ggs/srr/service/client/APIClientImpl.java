package ggs.srr.service.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.service.client.dto.UserDto;
import ggs.srr.service.client.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.client.dto.UsersRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class APIClientImpl implements APIClient{

    private final String FETCHPROJECT_URL_BASE = "http://localhost:8081";
    private final String TURBOFETCH_URL_BASE = "http://localhost:8082";
    private final String BASE_URL_OF_42 = "https://api.intra.42.fr";

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
    public List<UserDto> fetchUsersFromTurbofetch(String code) {
        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder
                .fromHttpUrl(TURBOFETCH_URL_BASE)
                .path("/users")
                .queryParam("code", code)
                .encode()
                .build()
                .toUriString();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
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
