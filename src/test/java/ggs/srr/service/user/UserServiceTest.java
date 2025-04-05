package ggs.srr.service.user;

import ggs.srr.domain.user.User;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

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

    private User createUserBy(String intraId, double level, int wallet, int collectionPoint) {
        return User.builder()
                .intraId(intraId)
                .level(level)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}