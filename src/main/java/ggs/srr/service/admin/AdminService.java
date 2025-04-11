package ggs.srr.service.admin;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.Role;
import ggs.srr.domain.user.User;
import ggs.srr.exception.user.UserErrorCode;
import ggs.srr.exception.user.UserException;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.system.dto.UserDto;
import ggs.srr.service.system.dto.UserProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectUserRepository projectUserRepository;

    @Value("admin")
    private List<String> adminIntraIds;

    public void saveUserInformation(List<UserDto> userDtos) {
        for (UserDto userDto : userDtos) {
            Optional<User> optional = userRepository.findByIntraId(userDto.getIntra_id());
            if (optional.isEmpty()) {
                User createdUser = toUserEntity(userDto);
                userRepository.save(createdUser);
                continue;
            }

            User findUser = optional.get();
            findUser.updateInformation(userDto.getLevel(), userDto.getWallet(), userDto.getCorrection_point());
        }
    }

    private User toUserEntity(UserDto dto) {
        return User.builder()
                .ftServerId(dto.getFt_server_id())
                .intraId(dto.getIntra_id())
                .role(getRole(dto.getIntra_id()))
                .wallet(dto.getWallet())
                .correctionPoint(dto.getCorrection_point())
                .level(dto.getLevel())
                .image(dto.getImage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public Role getRole(String intraId) {
        if (adminIntraIds.contains(intraId)) {
            return Role.ADMIN;
        }
        return Role.CADET;
    }

    public void saveProjectsInformation(List<String> projectNames) {
        projectNames.stream()
                .map(projectName -> createProject(projectName))
                .forEach(p -> projectRepository.save(p));
    }

    private Project createProject(String name) {
        Project newProject = Project.builder()
                .name(name)
                .build();

        projectRepository.save(newProject);
        return newProject;
    }

    public void initializeAllProjectUsers(List<UserProjectResponse.UsersProjectsResponse> usersProjectsResponses) {
        usersProjectsResponses.stream()
                .forEach(response -> {

                    String intraId = response.getIntraId();

                    response.getAllProjectsResponse().stream()
                            .forEach(
                                    userProjectsResponse -> {
                                        String projectName = userProjectsResponse.getProjectName();
                                        Optional<Project> optional = projectRepository.findByName(projectName);
                                        log.debug("optional status = {}", optional.isEmpty());
                                        Project project = optional.orElseGet(() -> createProject(projectName));
                                        User user = userRepository.findByIntraId(intraId).orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

                                        ProjectUser projectUser = ProjectUser.builder()
                                                .user(user)
                                                .project(project)
                                                .finalMark(userProjectsResponse.getProjectFinalMark() == null ? 0 : userProjectsResponse.getProjectFinalMark())
                                                .status(ProjectUserStatus.getByText(userProjectsResponse.getProjectStatus()))
                                                .build();

                                        projectUserRepository.save(projectUser);
                                    }
                            );
                });
    }

    public void deleteAllUserProjects() {
        projectUserRepository.deleteAll();
    }
}



