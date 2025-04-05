package ggs.srr.repository.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.common.FindByNullException;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectUserRepositoryTest {

    @Autowired
    ProjectUserRepository projectUserRepository;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    ProjectRepository projectRepository;

    @DisplayName("사용자의 프로젝트를 저장 및 조회할 수 있다.")
    @Test
    void saveAndFind() {
        //given
        User user = createUser("test");
        Project project = createProject("test");
        ProjectUser projectUser = createProjectUser(user, project, ProjectUserStatus.DEFAULT);

        userRepository.save(user);
        projectRepository.save(project);
        projectUserRepository.save(projectUser);

        //when
        ProjectUser findProjectUser = projectUserRepository.findById(projectUser.getId()).get();

        //then
        assertThat(findProjectUser).isNotNull();
        assertThat(findProjectUser.getId()).isEqualTo(projectUser.getId());
        assertThat(findProjectUser.getUser().getId()).isEqualTo(projectUser.getUser().getId());
        assertThat(findProjectUser.getProject().getId()).isEqualTo(projectUser.getProject().getId());

    }

    @DisplayName("사용자의 프로젝트를 조회시 null 을 대입할 수 없다.")
    @Test
    void findByNull() {
        //when //then
        assertThatThrownBy(() -> projectUserRepository.findById(null))
                .isInstanceOf(FindByNullException.class)
                .hasMessage("사용자의 프로젝트를 조회시 null 을 대입할 수 없습니다.");
    }

    @DisplayName("모든 사용자의 프로젝트를 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        User user = createUser("test");
        Project project1 = createProject("test project 1");
        Project project2 = createProject("test project 2");

        ProjectUser projectUser1 = createProjectUser(user, project1, ProjectUserStatus.IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, ProjectUserStatus.IN_PROGRESS);

        userRepository.save(user);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);

        //when
        List<ProjectUser> userProjects = projectUserRepository.findAll();

        //then
        assertThat(userProjects).hasSize(2);
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