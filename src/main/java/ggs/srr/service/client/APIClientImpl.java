package ggs.srr.service.client;

import static ggs.srr.exception.user.UserErrorCode.ACCESS_TOKEN_EXPIRED;
import static ggs.srr.exception.user.UserErrorCode.API_FETCH_FAILED;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.exception.user.UserException;
import ggs.srr.service.client.dto.ProjectDetailInfo;
import ggs.srr.service.client.dto.UserDto;
import ggs.srr.service.client.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.client.dto.UserProfileUpdate;
import ggs.srr.service.client.dto.UsersRequest;
import ggs.srr.service.user.request.UserUpdateServiceRequest;
import java.util.ArrayList;
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
   // private final String BASE_URL_OF_42 = "https://api.intra.42.fr";
    private final String USER_URL_PREFIX = "https://api.intra.42.fr/v2/users/";
    private final String PROJECT_USER_URL_SUFFIX = "/projects_users?filter[cursus]=21&page=";

    @Override
    public List<String> fetchProjectsFromFetchProject() {
        String url = FETCHPROJECT_URL_BASE + "/in_progress_projects";
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
            log.error("Error fetching in_progress_projects from FetchProject: {}", e.getResponseBodyAsString());
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
            log.error("Error fetching user in_progress_projects from FetchProject: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    @Override
    public UserProfileUpdate fetchUserUpdatableInformation(UserUpdateServiceRequest request) {
        //String oAuth2RefreshToken = request.getOAuth2RefreshToken();
        String oAuth2AccessToken = request.getOAuth2AccessToken();
        Long targetFtServerId = request.getTargetFtServerId();

        Map<String, Object> userProfile = fetchUserProfileFrom42API(oAuth2AccessToken, targetFtServerId);
        log.info("User info fetched: level={}, wallet={}, correctionPoint={}",
                userProfile.get("level"), userProfile.get("wallet"), userProfile.get("correction_point"));

        List<ProjectDetailInfo> projects = fetchUserProjectsFrom42API(oAuth2AccessToken, targetFtServerId);
        log.info("Projects fetched: {} projects found", projects.size());
        return createUserProfileUpdate(userProfile, projects);
    }

    private Map<String, Object> fetchUserProfileFrom42API(String accessToken, Long targetFtServerId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createAuthHeader(accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        try {
            String url = USER_URL_PREFIX + targetFtServerId;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> profile = response.getBody();

            if (profile != null) {
                extractLevelFromProfile(profile);
            }

            return profile;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.error("Unauthorized access to 42 API. Token might be expired. Please re-login.");
                throw new UserException(ACCESS_TOKEN_EXPIRED);
            }
            log.error("API fetch failed from 42 API: {}", e.getResponseBodyAsString());
            throw new UserException(API_FETCH_FAILED);
        }
    }

    private List<ProjectDetailInfo> fetchUserProjectsFrom42API(String accessToken, Long targetFtServerId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = createAuthHeader(accessToken);
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
        List<ProjectDetailInfo> allProjects = new ArrayList<>();

        int page = 1;
        while (true) {
            try {
                String url = USER_URL_PREFIX + targetFtServerId + PROJECT_USER_URL_SUFFIX + page;
                ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<List<Map<String, Object>>>() {}
                );

                List<Map<String, Object>> body = response.getBody();
                if (body == null || body.isEmpty()) {
                    break;
                }

                for (Map<String, Object> data : body) {
                    ProjectDetailInfo project = parseProjectData(data);
                    allProjects.add(project);
                }

                page++;
                delayRequest();
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    log.error("Unauthorized access to 42 API. Token might be expired. Please re-login.");
                    throw new UserException(ACCESS_TOKEN_EXPIRED);
                }
                log.error("API fetch failed from 42 API: {}", e.getResponseBodyAsString());
                throw new UserException(API_FETCH_FAILED);
            }
        }

        return allProjects;
    }

    private UserProfileUpdate createUserProfileUpdate(Map<String, Object> userProfile,
                                                      List<ProjectDetailInfo> projects) {
        double level = Double.parseDouble(userProfile.get("level").toString());
        int wallet = Integer.parseInt(userProfile.get("wallet").toString());
        int correctionPoint = Integer.parseInt(userProfile.get("correction_point").toString());

        List<ProjectDetailInfo> inProgressProjects = new ArrayList<>();
        List<ProjectDetailInfo> finishedProjects = new ArrayList<>();

        for (ProjectDetailInfo project : projects) {
            String status = project.getProjectStatus();
            if ("in_progress".equals(status)) {
                inProgressProjects.add(project);
            } else if ("finished".equals(status)) {
                finishedProjects.add(project);
            }
        }

        return new UserProfileUpdate(level, correctionPoint, wallet, inProgressProjects, finishedProjects);
    }

    private HttpHeaders createAuthHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private void extractLevelFromProfile(Map<String, Object> profile) {
        if (profile.containsKey("level")) {
            log.info("Level already exists at top level: {}", profile.get("level"));
            return;
        }

        if (!profile.containsKey("cursus_users")) {
            log.warn("No cursus_users found in profile");
            return;
        }

        List<Map<String, Object>> cursusUsers = (List<Map<String, Object>>) profile.get("cursus_users");
        if (cursusUsers == null || cursusUsers.isEmpty()) {
            log.warn("cursus_users is null or empty");
            return;
        }

        Double level = null;
        for (Map<String, Object> cursusUser : cursusUsers) {
            Map<String, Object> cursus = (Map<String, Object>) cursusUser.get("cursus");
            if (cursus != null) {
                Object cursusId = cursus.get("id");
                log.debug("Found cursus with id: {}", cursusId);

                if (cursusId != null && ("21".equals(cursusId.toString()) || Integer.valueOf(21).equals(cursusId))) {
                    Object levelObj = cursusUser.get("level");
                    if (levelObj != null) {
                        level = Double.parseDouble(levelObj.toString());
                        log.info("Found level {} in cursus_id 21", level);
                        break;
                    }
                }
            }
        }

        if (level == null && !cursusUsers.isEmpty()) {
            Map<String, Object> lastCursus = cursusUsers.get(cursusUsers.size() - 1);
            Object levelObj = lastCursus.get("level");
            if (levelObj != null) {
                level = Double.parseDouble(levelObj.toString());
                log.info("Using level {} from last cursus", level);
            }
        }

        if (level != null) {
            profile.put("level", level);
        } else {
            log.error("Could not find level in any cursus_users");
            profile.put("level", 0.0); // 기본값 설정
        }
    }

    private ProjectDetailInfo parseProjectData(Map<String, Object> data) {
        Map<String, Object> project = (Map<String, Object>) data.get("project");
        String projectName = (String) project.get("name");

        Integer finalMark = null;
        String status = null;


        List<Map<String, Object>> teams = (List<Map<String, Object>>) data.get("teams");
        if (teams != null && !teams.isEmpty()) {
            Map<String, Object> lastTeam = teams.get(teams.size() - 1);
            finalMark = (Integer) lastTeam.get("final_mark");
            status = (String) lastTeam.get("status");
        }
        log.info("finalMark: {}", finalMark);
        if (finalMark == null) finalMark = 0;
        return new ProjectDetailInfo(projectName, finalMark, status);
    }

    private void delayRequest() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            log.warn("Thread interrupted while fetching user in_progress_projects from 42 API.");
        }
    }
}
