package ggs.srr.service.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.projectuser.request.ProjectUserRequest;
import ggs.srr.service.projectuser.request.ProjectUsersRequest;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import ggs.srr.service.projectuser.response.ProjectUserInformationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static ggs.srr.domain.projectuser.ProjectUserStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectUserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectUserRepository projectUserRepository;

    @Autowired
    ProjectUserService projectUserService;

    @DisplayName("모든 사용자의 프로젝트의 분포를 확인할 수 있어야 한다.")
    @Test
    void getProjectUserDistribution() {
        //given
        User user1 = createUser("test1");
        User user2 = createUser("test2");

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");

        ProjectUser pu1 = createProjectUser(user1, project1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user1, project2, FINISHED);

        ProjectUser pu3 = createProjectUser(user2, project1, FINISHED);
        ProjectUser pu4 = createProjectUser(user2, project2, FINISHED);

        userRepository.save(user1);
        userRepository.save(user2);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        projectUserRepository.save(pu3);
        projectUserRepository.save(pu4);

        //when
        ProjectUserDistributionResponse projectUserDistribution = projectUserService.getProjectUserDistribution();
        Map<String, Map<ProjectUserStatus, Long>> content = projectUserDistribution.getDistribution();

        //then
        assertThat(content.get(project1.getName()).get(IN_PROGRESS)).isEqualTo(1);
        assertThat(content.get(project1.getName()).get(FINISHED)).isEqualTo(1);
        assertThat(content.get(project2.getName()).get(IN_PROGRESS)).isEqualTo(0);
        assertThat(content.get(project2.getName()).get(FINISHED)).isEqualTo(2);
    }

    @DisplayName("DEFAULT 타입으로 사용자의 프로젝트를 조회할 경우 진행중, 모든 사용자 프로젝트가 조회된다.")
    @Test
    void getUserDefaultProjects() {
        //given
        User user1 = createUser("test1");
        User user2 = createUser("test2");

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");

        ProjectUser pu1 = createProjectUser(user1, project1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user1, project2, FINISHED);

        ProjectUser pu3 = createProjectUser(user2, project1, FINISHED);
        ProjectUser pu4 = createProjectUser(user2, project2, FINISHED);

        userRepository.save(user1);
        userRepository.save(user2);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        projectUserRepository.save(pu3);
        projectUserRepository.save(pu4);

        ProjectUsersRequest request = new ProjectUsersRequest(user1.getId(), DEFAULT);

        //when

        List<ProjectUserInformationResponse> response = projectUserService.getUserProjects(request);

        //then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getStatus()).isSameAs(IN_PROGRESS);
        assertThat(response.get(1).getStatus()).isSameAs(FINISHED);
    }

    @DisplayName("IN_PROGRESS 타입으로 사용자의 프로젝트를 조회할 경우 진행중, 완프로젝트가 조회된다.")
    @Test
    void getUserInProgressProjects() {
        //given
        User user1 = createUser("test1");
        User user2 = createUser("test2");

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");

        ProjectUser pu1 = createProjectUser(user1, project1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user1, project2, FINISHED);

        ProjectUser pu3 = createProjectUser(user2, project1, FINISHED);
        ProjectUser pu4 = createProjectUser(user2, project2, FINISHED);

        userRepository.save(user1);
        userRepository.save(user2);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        projectUserRepository.save(pu3);
        projectUserRepository.save(pu4);

        ProjectUsersRequest request = new ProjectUsersRequest(user1.getId(), IN_PROGRESS);

        //when

        List<ProjectUserInformationResponse> response = projectUserService.getUserProjects(request);

        //then
        assertThat(response).hasSize(1);
        assertThat(response.get(0).getStatus()).isSameAs(IN_PROGRESS);
    }

    @DisplayName("FINISHED 타입으로 사용자의 프로젝트를 조회할 경우 진행중, 완료 상태의 모든 사용자 프로젝트가 조회된다.")
    @Test
    void getUserFinishedProjects() {
        //given
        User user1 = createUser("test1");
        User user2 = createUser("test2");

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");

        ProjectUser pu1 = createProjectUser(user1, project1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user1, project2, FINISHED);

        ProjectUser pu3 = createProjectUser(user2, project1, FINISHED);
        ProjectUser pu4 = createProjectUser(user2, project2, FINISHED);

        userRepository.save(user1);
        userRepository.save(user2);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        projectUserRepository.save(pu3);
        projectUserRepository.save(pu4);

        ProjectUsersRequest request = new ProjectUsersRequest(user2.getId(), FINISHED);

        //when

        List<ProjectUserInformationResponse> response = projectUserService.getUserProjects(request);

        //then
        assertThat(response).hasSize(2);
        assertThat(response.get(0).getStatus()).isSameAs(FINISHED);
        assertThat(response.get(1).getStatus()).isSameAs(FINISHED);
    }

    @DisplayName("사용자 id 와 프로젝트 id 를 통해사용자의 진행중인 프로젝트 단건 조회가 가능하다.")
    @Test
    void getUserProject() {
        //given
        User user = createUser("test1");

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");

        ProjectUser pu1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user, project2, FINISHED);

        userRepository.save(user);
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);

        ProjectUserRequest request = new ProjectUserRequest(user.getId(), project1.getId());

        //when
        ProjectUserInformationResponse response = projectUserService.getUserProject(request);

        //then
        assertThat(response.getProjectName()).isEqualTo(project1.getName());
        assertThat(response.getStatus()).isEqualTo(pu1.getStatus());
        assertThat(response.getFinalMark()).isEqualTo(pu1.getFinalMark());
    }

    private User createUser(String intraId) {
        return User.builder()
                .intraId(intraId)
                .build();
    }

    private Project createProject(String projectName) {
        return Project.builder()
                .name(projectName)
                .build();
    }

    private ProjectUser createProjectUser(User user, Project project, ProjectUserStatus status) {
        return ProjectUser
                .builder()
                .user(user)
                .project(project)
                .status(status)
                .finalMark(100)
                .build();
    }



}