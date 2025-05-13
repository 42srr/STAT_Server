package ggs.srr.service.user;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.exception.project.ProjectException;
import ggs.srr.exception.user.UserErrorCode;
import ggs.srr.exception.user.UserException;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.client.dto.ProjectDetailInfo;
import ggs.srr.service.client.dto.UserProfileUpdate;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import ggs.srr.service.user.response.UserUpdateResponse;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static ggs.srr.domain.projectuser.ProjectUserStatus.FINISHED;
import static ggs.srr.domain.projectuser.ProjectUserStatus.IN_PROGRESS;
import static ggs.srr.exception.project.ProjectErrorCode.NOT_FOUND_PROJECT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectUserRepository projectUserRepository;

    @Autowired
    UserService userService;

    @DisplayName("마지막 업데이트가 12 시간이 안된 사용자는 updatable 이 false 이다.")
    @Test
    void notUpdatableUser() {
        //given

        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime requestTime = LocalDateTime.of(2025, 4, 1, 11, 59, 59);

        User user = User.builder()
                .updatedAt(updatedAt).build();

        userRepository.save(user);

        UserInformationServiceRequest request = new UserInformationServiceRequest(user.getId());

        //when
        UserInformationResponse response = userService.findById(request, requestTime);

        //then
        assertThat(response.isUpdatable()).isFalse();
    }

    @DisplayName("마지막 업데이트가 12 시간이 지난 사용자는 updatable 이 true 이다.")
    @Test
    void updatableUser() {
        //given

        LocalDateTime updatedAt = LocalDateTime.of(2025, 4, 1, 0, 0, 0);
        LocalDateTime requestTime = LocalDateTime.of(2025, 4, 1, 12, 0, 0);

        User user = User.builder()
                .updatedAt(updatedAt).build();

        userRepository.save(user);

        UserInformationServiceRequest request = new UserInformationServiceRequest(user.getId());

        //when
        UserInformationResponse response = userService.findById(request, requestTime);

        //then
        assertThat(response.isUpdatable()).isTrue();
    }

    @DisplayName("레벨별 사용자 분포를 확인할 수 있어야 한다.")
    @Test
    void getUserLevelDistribution() {
        //given
        User user1 = createUserBy("test1", 1.2, 0, 0);
        User user2 = createUserBy("test2", 1.7, 0, 0);
        User user3 = createUserBy("test3", 2.1,0, 0);
        User user4 = createUserBy("test4", 4.2,0, 0);
        User user5 = createUserBy("test5", 5.2,0, 0);
        User user6 = createUserBy("test6", 5.9,0, 0);

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);
        userRepository.save(user6);

        //when
        LevelDistributionResponse userLevelDistribution = userService.getUserLevelDistribution();

        //then
        assertThat(userLevelDistribution.getDistribution().get(1)).isEqualTo(2);
        assertThat(userLevelDistribution.getDistribution().get(2)).isEqualTo(1);
        assertThat(userLevelDistribution.getDistribution().get(4)).isEqualTo(1);
        assertThat(userLevelDistribution.getDistribution().get(5)).isEqualTo(2);
    }

    @DisplayName("사용자의 level ranking 순위를 제공한다.")
    @Test
    void getUserRankingOfLevel() {
        //given
        for (int i = 0; i < 20; i++) {
            User user = createUserBy("test " + i, 2.0 + i / 10.0, 0, 0);
            userRepository.save(user);
        }

        UserRankingServiceRequest request = new UserRankingServiceRequest(0, 10);

        //when
        List<UserInformationResponse> userLevelRanking = userService.getUserRankingOfLevel(request);

        //then
        assertThat(userLevelRanking.size()).isLessThanOrEqualTo(10);
        assertThat(userLevelRanking).isSortedAccordingTo((r1, r2) -> Double.compare(r2.getLevel(), r1.getLevel()));
    }

    @DisplayName("사용자의 level ranking 순위를 제공한다.")
    @Test
    void getUserRankingOfWallet() {
        //given
        for (int i = 0; i < 20; i++) {
            User user = createUserBy("test " + i, 2.0 + i / 10.0, i + 3, 0);
            userRepository.save(user);
        }

        UserRankingServiceRequest request = new UserRankingServiceRequest(0, 10);

        //when
        List<UserInformationResponse> userLevelRanking = userService.getUserRankingOfWallet(request);

        //then
        assertThat(userLevelRanking.size()).isLessThanOrEqualTo(10);
        assertThat(userLevelRanking).isSortedAccordingTo((r1, r2) -> r2.getWallet() - r1.getWallet());
    }

    @DisplayName("사용자의 level ranking 순위를 제공한다.")
    @Test
    void getUserRankingOfCollectionPoint() {
        //given
        for (int i = 0; i < 20; i++) {
            User user = createUserBy("test " + i, 2.0 + i / 10.0, 0, i + 10);
            userRepository.save(user);
        }

        UserRankingServiceRequest request = new UserRankingServiceRequest(0, 10);

        //when
        List<UserInformationResponse> userLevelRanking = userService.getUserRankingOfCollectionPoint(request);

        //then
        assertThat(userLevelRanking.size()).isLessThanOrEqualTo(10);
        assertThat(userLevelRanking).isSortedAccordingTo((r1, r2) -> r2.getCollectionPoint() - r1.getCollectionPoint());
    }

    @DisplayName("존재하지 않는 요청자로 업데이트 요청 준비시 예외 발생")
    @Test
    void getUserUpdateRequest_RequesterNotFound() {
        // given
        User target = createUserBy("target", 1.2, 0, 0);
        userRepository.save(target);

        // when & then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserUpdateRequest("nonexistent", target.getId());
        });

        assertThat(exception.getMessage()).isEqualTo(UserErrorCode.NOT_FOUND_USER.getMessage());
    }

    @DisplayName("존재하지 않는 대상 사용자로 업데이트 요청 준비시 예외 발생")
    @Test
    void getUserUpdateRequest_TargetUserNotFound() {
        // given
        User requester = createUserBy("requester", 1.2, 0, 0);
        userRepository.save(requester);

        // when & then
        UserException exception = assertThrows(UserException.class, () -> {
            userService.getUserUpdateRequest(requester.getIntraId(), 999L);
        });

        assertThat(exception.getMessage()).isEqualTo(UserErrorCode.NOT_FOUND_USER.getMessage());
    }

    @DisplayName("사용자 프로필 정보를 성공적으로 업데이트한다")
    @Test
    void updateUserProfile_Success() {
        // given
        User user = createUserBy("test", 6.5, 10, 5);
        userRepository.save(user);

        Project project1 = createProject("p1");
        Project project2 = createProject("p2");
        projectRepository.save(project1);
        projectRepository.save(project2);

        List<ProjectDetailInfo> inProgressProjects = new ArrayList<>();
        inProgressProjects.add(new ProjectDetailInfo("p1", 0, "inProgress"));

        List<ProjectDetailInfo> finishedProjects = new ArrayList<>();
        finishedProjects.add(new ProjectDetailInfo("p2", 100, "finished"));

        UserProfileUpdate userProfileUpdate = new UserProfileUpdate(
                7.0, 10, 15, inProgressProjects, finishedProjects);

        // when
        UserUpdateResponse response = userService.updateUserProfile(user.getId(), userProfileUpdate);

        // then
        assertThat(user.getLevel()).isEqualTo(7.0);
        assertThat(user.getWallet()).isEqualTo(15);
        assertThat(user.getCorrectionPoint()).isEqualTo(10);

        List<ProjectUser> inProgressProjectUsers = projectUserRepository.findByUserIdAndStatus(
                user.getId(), IN_PROGRESS);
        List<ProjectUser> finishedProjectUsers = projectUserRepository.findByUserIdAndStatus(
                user.getId(), FINISHED);

        assertThat(inProgressProjectUsers).hasSize(1);
        assertThat(inProgressProjectUsers.get(0).getProject().getName()).isEqualTo("p1");
        assertThat(inProgressProjectUsers.get(0).getFinalMark()).isEqualTo(0);

        assertThat(finishedProjectUsers).hasSize(1);
        assertThat(finishedProjectUsers.get(0).getProject().getName()).isEqualTo("p2");
        assertThat(finishedProjectUsers.get(0).getFinalMark()).isEqualTo(100);

        assertThat(response).isNotNull();
        assertThat(response.getLevel()).isEqualTo(7.0);
        assertThat(response.getWallet()).isEqualTo(15);
        assertThat(response.getCollectionPoint()).isEqualTo(10);
        assertThat(response.isUpdatable()).isFalse();
        assertThat(response.getInProgressProjects()).hasSize(1);
        assertThat(response.getFinishedProjects()).hasSize(1);
    }

    @DisplayName("존재하지 않는 프로젝트로 사용자 프로필 업데이트 시 예외 발생")
    @Test
    void updateUserProfile_ProjectNotFound() {
        // given
        User user = createUserBy("test", 6.5, 10, 5);
        userRepository.save(user);

        List<ProjectDetailInfo> inProgressProjects = new ArrayList<>();
        inProgressProjects.add(new ProjectDetailInfo("nonexistent", 0,"in_progress"));

        UserProfileUpdate userProfileUpdate = new UserProfileUpdate(
                7.0, 150, 10, inProgressProjects, new ArrayList<>());

        // when & then
        ProjectException exception = assertThrows(ProjectException.class, () -> {
            userService.updateUserProfile(user.getId(), userProfileUpdate);
        });

        assertThat(exception.getMessage()).isEqualTo(NOT_FOUND_PROJECT.getMessage());
    }

        private User createUserBy(String intraId, double level, int wallet, int collectionPoint) {
        return User.builder()
                .intraId(intraId)
                .level(level)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private Project createProject(String projectName) {
        return Project.builder()
                .name(projectName)
                .build();
    }
}