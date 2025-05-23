package ggs.srr.repository.projectuser;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.FindByNullException;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ggs.srr.domain.projectuser.ProjectUserStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        ProjectUser projectUser = createProjectUser(user, project, DEFAULT);

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

        ProjectUser projectUser1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, IN_PROGRESS);

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

    @DisplayName("모든 사용자의 종료 프로젝트를 조회할 수 있다.")
    @Test
    void findFinished() {
        //given
        User user = createUser("test");
        Project project1 = createProject("test project 1");
        Project project2 = createProject("test project 2");

        ProjectUser projectUser1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, FINISHED);

        userRepository.save(user);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);

        //when
        List<ProjectUser> userProjects = projectUserRepository.findFinished();

        //then
        assertThat(userProjects).hasSize(1);
    }

    @DisplayName("모든 사용자의 진행 프로젝트를 조회할 수 있다.")
    @Test
    void findInProgress() {
        //given
        User user = createUser("test");
        Project project1 = createProject("test project 1");
        Project project2 = createProject("test project 2");

        ProjectUser projectUser1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, FINISHED);

        userRepository.save(user);

        projectRepository.save(project1);
        projectRepository.save(project2);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);

        //when
        List<ProjectUser> userProjects = projectUserRepository.findInProgress();

        //then
        assertThat(userProjects).hasSize(1);
    }

    @DisplayName("사용자의 프로젝트를 사용자 아이디와 type 으로 조회할 수 있다.")
    @Test
    void findByUserIdAndStatus() {
        //given
        User user = createUser("test");
        Project project1 = createProject("test project 1");
        Project project2 = createProject("test project 2");
        Project project3 = createProject("test project 3");

        ProjectUser projectUser1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, IN_PROGRESS);
        ProjectUser projectUser3 = createProjectUser(user, project3, FINISHED);

        userRepository.save(user);

        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);
        projectUserRepository.save(projectUser3);

        //when

        List<ProjectUser> projectUsers = projectUserRepository.findByUserIdAndStatus(user.getId(), IN_PROGRESS);

        //then
        assertThat(projectUsers).hasSize(2);
    }

    @DisplayName("사용자의 프로젝트를 사용자 아이디로 조회할 수 있다.")
    @Test
    void findByUserId() {
        //given
        User user = createUser("test");
        Project project1 = createProject("test project 1");
        Project project2 = createProject("test project 2");
        Project project3 = createProject("test project 3");

        ProjectUser projectUser1 = createProjectUser(user, project1, IN_PROGRESS);
        ProjectUser projectUser2 = createProjectUser(user, project2, IN_PROGRESS);
        ProjectUser projectUser3 = createProjectUser(user, project3, FINISHED);

        userRepository.save(user);

        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);
        projectUserRepository.save(projectUser3);

        //when

        List<ProjectUser> projectUsers = projectUserRepository.findByUserId(user.getId());

        //then
        assertThat(projectUsers).hasSize(3);
    }

    @DisplayName("사용자의 아이디와 프로젝트의 아이디를 통해 사용자가 진행중인 프로젝트를 조회할 수 있다.")
    @Test
    void findByUserIdAndProjectId() {
        //given
        User user = createUser("test");
        Project p1 = createProject("p1");
        Project p2 = createProject("p2");

        userRepository.save(user);
        projectRepository.save(p1);
        projectRepository.save(p2);

        ProjectUser pu1 = createProjectUser(user, p1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(user, p2, IN_PROGRESS);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        //when
        ProjectUser findPu = projectUserRepository.findByUserIdAdnProjectId(user.getId(), p1.getId()).get();

        //then
        assertThat(findPu).isNotNull();
        assertThat(findPu.getUser().getId()).isEqualTo(user.getId());
        assertThat(findPu.getProject().getId()).isEqualTo(p1.getId());
    }

    @DisplayName("모든 사용자의 프로젝트를 삭제할 수 있다.")
    @Test
    void deleteAll() {
        //given
        User user = createUser("test");

        Project p1 = createProject("test1");
        Project p2 = createProject("test2");
        Project p3 = createProject("test3");
        Project p4 = createProject("test4");

        ProjectUser projectUser1 = createProjectUser(user, p1, FINISHED);
        ProjectUser projectUser2 = createProjectUser(user, p2, FINISHED);
        ProjectUser projectUser3 = createProjectUser(user, p3, FINISHED);
        ProjectUser projectUser4 = createProjectUser(user, p4, FINISHED);

        userRepository.save(user);

        projectRepository.save(p1);
        projectRepository.save(p2);
        projectRepository.save(p3);
        projectRepository.save(p4);

        projectUserRepository.save(projectUser1);
        projectUserRepository.save(projectUser2);
        projectUserRepository.save(projectUser3);
        projectUserRepository.save(projectUser4);

        projectUserRepository.deleteAll();
        //when
        List<ProjectUser> all = projectUserRepository.findAll();

        //then
        assertThat(all).isEmpty();
    }

    @Test
    void deleteById() {
        User test1 = createUser("test1");
        User test2 = createUser("test2");

        Project tp1 = createProject("test1");
        Project tp2 = createProject("test2");

        ProjectUser pu1 = createProjectUser(test1, tp1, IN_PROGRESS);
        ProjectUser pu2 = createProjectUser(test1, tp2, FINISHED);
        ProjectUser pu3 = createProjectUser(test2, tp1, IN_PROGRESS);

        userRepository.save(test1);
        userRepository.save(test2);

        projectRepository.save(tp1);
        projectRepository.save(tp2);

        projectUserRepository.save(pu1);
        projectUserRepository.save(pu2);
        projectUserRepository.save(pu3);

        projectUserRepository.deleteById(test1.getId());

        List<ProjectUser> result = projectUserRepository.findAll();

        assertThat(result).hasSize(1);
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