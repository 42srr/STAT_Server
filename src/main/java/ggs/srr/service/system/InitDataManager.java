package ggs.srr.service.system;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.domain.user.Role;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.system.dto.ParsingResponseDto;
import ggs.srr.service.system.dto.UserDto;
import ggs.srr.service.system.dto.UserProjectResponse.UsersProjectsResponse;
import ggs.srr.service.system.dto.UserContent;
import ggs.srr.service.system.dto.UsersRequest;
import ggs.srr.service.system.exception.NotFoundAdminUserException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InitDataManager {

    private final APIClient apiClient;
    private final String NOT_FOUND_ADMIN_USER_EXCEPTION_MESSAGE = "관리자를 찾을 수 없습니다.";

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

    public void initProjects() {
        List<String> projects = apiClient.fetchProjectsFromFetchProject();
        if (projects.isEmpty()) {
        }
        //디비에 저장하는 로직 필요
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
            long startTime = System.currentTimeMillis();
            List<UserDto> users = apiClient.fetchUsersFromTurbofetch();
            if (users.isEmpty()) {
                return;
            }
            persistUsers(users);
            long endTime = System.currentTimeMillis();
            log.info("time = {}", endTime - startTime);

        } catch (HttpClientErrorException e) {
            log.info("access token expire!! 갱신 로직 추가 필요");
            throw new RuntimeException("access token expired");
        }
    }

    private void persistAllUserProjects(String oAuth2AccessToken) {
        try {
            List<FtUser> allUsers = userRepository.findAll();
            int count = 0;
            for (FtUser user : allUsers) {
                String serverId = String.valueOf(user.getFtServerId());
                UserContent userContent = new UserContent(user.getIntraId(), serverId);
                List<UserContent> userIds = new ArrayList<>();
                userIds.add(userContent);
                count++;
                UsersRequest usersRequest = new UsersRequest(userIds);
                List<UsersProjectsResponse> body = apiClient.fetchUserProjectsFromFetchProject(usersRequest);
                log.info("response getBody: {}", body);

                if (body == null || body.isEmpty()) {
                    log.info("body is empty");
                    continue;
                }
                persisUserProjects(body, user);
                Thread.sleep(1000);
            }

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            log.info("access token expire!! 갱신 로직 추가 필요");
            throw new RuntimeException("access token expired");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void persisUserProjects(List<UsersProjectsResponse> responseBody, FtUser user) {
        for (UsersProjectsResponse rawProjectUser : responseBody) {
            for (ParsingResponseDto projectDto : rawProjectUser.getAllProjectsResponse()) {
                //if (!isMainCourse(rawProjectUser)) {
                //    continue;
                //}
                Project project = parseToProject(projectDto);
                ProjectUser projectUser = new ProjectUser();
                projectUser.initUser(user);
                projectUser.initProject(project);
                projectUser.initStatus(projectDto.getProjectStatus());
                projectUserRepository.save(projectUser);
            }
        }
    }

    //private boolean isMainCourse(ArrayList<HashMap<String, Object>> rawProjectUser) {
    //    List<Integer> cursusIds = (List<Integer>) rawProjectUser.get("cursus_ids");
    //    return cursusIds.contains(21);
    //}

    private void persistUsers(List<UserDto> responseBody) {
        responseBody.stream()
                .map(this::convertToFtUser)
                .forEach(userRepository::save);
    }

    private FtUser convertToFtUser(UserDto userDto) {
        return new FtUser(
                userDto.getFt_server_id(),
                userDto.getIntra_id(),
                userDto.getRole(),
                userDto.getWallet(),
                userDto.getCorrection_point(),
                userDto.getLevel(),
                userDto.getImage()
        );
    }

    private Project parseToProject(ParsingResponseDto rawProjectUser) {
        String projectName = rawProjectUser.getProjectName();
        Optional<Project> optional = projectRepository.findByProjectName(projectName);

        if (optional.isPresent()) {
            return optional.get();
        }

        Project newProject = new Project(projectName);
        projectRepository.save(newProject);
        return newProject;
    }
}
