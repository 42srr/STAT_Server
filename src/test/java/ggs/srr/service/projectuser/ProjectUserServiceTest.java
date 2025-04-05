package ggs.srr.service.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

        System.out.println("content = " + content);

        //then
        assertThat(content.get(project1.getName()).get(IN_PROGRESS)).isEqualTo(1);
        assertThat(content.get(project1.getName()).get(FINISHED)).isEqualTo(1);
        assertThat(content.get(project2.getName()).get(IN_PROGRESS)).isEqualTo(0);
        assertThat(content.get(project2.getName()).get(FINISHED)).isEqualTo(2);
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
                .build();
    }



}