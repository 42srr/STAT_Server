package ggs.srr.service.system;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.domain.user.Role;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.system.exception.NotFoundAdminUserException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class InitDataManager {

    private final String CAMPUS_USER_URI = "https://api.intra.42.fr/v2/cursus_users?filter[campus_id]=69&filter[cursus_id]=21&page=";
    private final String PROJECT_USER_URL_PREFIX = "https://api.intra.42.fr/v2/users/";
    private final String PROJECT_USER_URL_SUFFIX = "/projects_users?filter[cursus]=21&page=";
    private final String NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE = "관리자를 찾을 수 없습니다.";
    private final List<String> balckList = List.of("3b3-179603","tacount", "3b3-179647", "gcoconut");

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

    public void initUser(String intraId) {

        String oAuth2AccessToken = getAdminOAuth2AccessToken(intraId);
        System.out.println("oAuth2AccessToken = " + oAuth2AccessToken);
        persistAllUsers(oAuth2AccessToken);

    }

    public void initProjectUser(String intraId) {
        String oAuth2AccessToken = getAdminOAuth2AccessToken(intraId);
        persistAllUserProjects(oAuth2AccessToken);
    }

    private String getAdminOAuth2AccessToken(String intraId) {
        Optional<FtUser> optional = userRepository.findByIntraId(intraId);
        if (optional.isEmpty()) {
            throw new NotFoundAdminUserException(NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE);
        }
        FtUser admin = optional.get();
        return admin.getOAuth2AccessToken();
    }


    private void persistAllUsers(String oAuth2AccessToken) {
        try {

            int page = 0;
            long startTime = System.currentTimeMillis();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + oAuth2AccessToken);
            HttpEntity request = new HttpEntity(headers);
            while (true) {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<ArrayList> response = restTemplate.exchange(CAMPUS_USER_URI + page, HttpMethod.GET,
                        request,
                        ArrayList.class);

                ArrayList<HashMap<String, Object>> body = response.getBody();

                if (isEmptyBody(body)) {
                    break;
                }

                persistUsers(body);
                page++;

            }
            long endTime = System.currentTimeMillis();
            log.info("time = {}", endTime - startTime);

        } catch (HttpClientErrorException e) {
            log.info("access token expire!! 갱신 로직 추가 필요");
            throw new RuntimeException("access token expired");
        }
    }

    private void persistAllUserProjects(String oAuth2AccessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + oAuth2AccessToken);
            HttpEntity request = new HttpEntity(headers);
            List<FtUser> users = userRepository.findAll();
            int count = 0;
            for (FtUser user : users) {
                log.info("username = {}", user.getIntraId());
                count++;
                int page = 0;
                log.info("{} / {}", count, users.size());
                while (true) {
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<ArrayList> response = restTemplate.exchange(PROJECT_USER_URL_PREFIX + user.getFtServerId() + PROJECT_USER_URL_SUFFIX + page, HttpMethod.GET,
                            request,
                            ArrayList.class);

                    ArrayList<HashMap<String, Object>> body = response.getBody();
                    if (isEmptyBody(body)) {
                        log.info("body is empty");
                        break;
                    }
                    persisUserProjects(body, user);
                    page++;
                    Thread.sleep(1000);
                }
            }

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.info("access token expire!! 갱신 로직 추가 필요");
            throw new RuntimeException("access token expired");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void persisUserProjects(ArrayList<HashMap<String, Object>> responseBody, FtUser user) {
        for (HashMap<String, Object> rawProjectUser : responseBody) {
            if (!isMainCourse(rawProjectUser)) {
                continue;
            }
            Project project = parseToProject(rawProjectUser);
            ProjectUser projectUser = new ProjectUser();
            projectUser.initUser(user);
            projectUser.initProject(project);
            projectUser.initStatus(rawProjectUser.get("status").toString());
            projectUserRepository.save(projectUser);
        }
    }

    private boolean isEmptyBody(ArrayList<HashMap<String, Object>> body) {
        return body == null || body.isEmpty();
    }

    private boolean isMainCourse(HashMap<String, Object> rawProjectUser) {
        List<Integer> cursusIds = (List<Integer>) rawProjectUser.get("cursus_ids");
        return cursusIds.contains(21);
    }

    private void persistUsers(ArrayList<HashMap<String, Object>> responseBody) {
        responseBody.stream()
                .filter(this::isStudent)
                .filter(this::isNotTestAccount)
                .filter(this::notInBlackList)
                .map(this::parseToUser)
                .forEach(userRepository::save);
    }

    private boolean isStudent(HashMap<String, Object> rawUser) {
        Map<String, Object> userData = (Map<String, Object>) rawUser.get("user");
        return !Boolean.parseBoolean(userData.get("staff?").toString());
    }

    private boolean isNotTestAccount(HashMap<String, Object> rawUser) {
        Map<String, Object> userData = (Map<String, Object>) rawUser.get("user");
        String intraId = userData.get("login").toString();
        return !intraId.contains("test");
    }

    private boolean notInBlackList(HashMap<String, Object> rawUser) {
        Map<String, Object> userData = (Map<String, Object>) rawUser.get("user");
        String intraId = userData.get("login").toString();
        return !balckList.contains(intraId);
    }

    private FtUser parseToUser(HashMap<String, Object> rawUser) {

        double level = Double.parseDouble(rawUser.get("level").toString());

        Map<String, Object> userData = (Map<String, Object>) rawUser.get("user");
        long ftServerId = Long.parseLong(userData.get("id").toString());
        String intraId = userData.get("login").toString();
        Role role = Role.CADET;
        int wallet = Integer.parseInt(userData.get("wallet").toString());
        int correctionPoint = Integer.parseInt(userData.get("correction_point").toString());
        String smallImageUrl = getSmallImageUrl((Map<String, Object>) userData.get("image"));

        return new FtUser(ftServerId, intraId, role, wallet, correctionPoint, level, smallImageUrl);
    }

    private String getSmallImageUrl(Map<String, Object> image) {
        Map<String, String> versions = (Map<String, String>) image.get("versions");
        return versions.get("small");
    }

    private Project parseToProject(HashMap<String, Object> rawProjectUser) {
        Map<String, String> project = (Map<String, String>) rawProjectUser.get("project");
        String projectName = project.get("name");

        Optional<Project> optional = projectRepository.findByProjectName(projectName);

        if (optional.isPresent()) {
            return optional.get();
        }

        Project newProject = new Project(projectName);
        projectRepository.save(newProject);
        return newProject;
    }

}
