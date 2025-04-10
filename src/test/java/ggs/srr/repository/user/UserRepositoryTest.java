package ggs.srr.repository.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.FindByNullException;
import ggs.srr.repository.user.dto.UserRankQueryDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Slf4j
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원을 저장 및 조회할 수 있다.")
    @Test
    void saveAndFind() {
        //given
        User user = User.builder()
                .intraId("test")
                .build();

        userRepository.save(user);

        //when
        User findUser = userRepository.findById(user.getId()).get();

        //then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(user.getId());
    }

    @DisplayName("회원 조회시 null 로 조회할 수 없다.")
    @Test
    void findByNull() {

        //when //then
        assertThatThrownBy(() -> userRepository.findById(null))
                .isInstanceOf(FindByNullException.class)
                .hasMessage("사용자 조회시 id 로 null 을 입력할 수 없습니다.");
    }

    @DisplayName("모든 회원을 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        User user1 = User.builder()
                .intraId("test1")
                .build();

        User user2 = User.builder()
                .intraId("test2")
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        //when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users).hasSize(2);
    }

    @DisplayName("사용자를 level 기준으로 정렬한 뒤 maxResult 보다 작은 수의 사용자를 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"0, 5", "1, 7", "1, 10"})
    void rankByLevel(int startPosition, int maxResult) {
        //given
        for (int i = 0; i < 17; i++) {
            User user = User.builder().intraId("user " + i).level(2 + (i / 10.0)).build();
            userRepository.save(user);
        }

        UserRankQueryDto dto = new UserRankQueryDto(startPosition, maxResult);

        //when
        List<User> rankedUsers = userRepository.getRankByLevel(dto);

        rankedUsers.stream()
                .forEach(u -> log.info("level = {}", u.getLevel()));

        //then
        assertThat(rankedUsers.size()).isLessThanOrEqualTo(maxResult);
        assertThat(rankedUsers).isSortedAccordingTo((u1, u2) -> Double.compare(u2.getLevel(), u1.getLevel()));
    }

    @DisplayName("사용자를 wallet 기준으로 정렬한 뒤 maxResult 보다 작은 수의 사용자를 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"0, 5", "1, 7", "1, 10"})
    void rankByWallet(int startPosition, int maxResult) {
        //given
        for (int i = 0; i < 17; i++) {
            User user = User.builder().intraId("user " + i).wallet(i).build();
            userRepository.save(user);
        }

        UserRankQueryDto dto = new UserRankQueryDto(startPosition, maxResult);

        //when
        List<User> rankedUsers = userRepository.getRankByWallet(dto);

        rankedUsers.stream()
                .forEach(u -> log.info("wallet = {}", u.getWallet()));

        //then
        assertThat(rankedUsers.size()).isLessThanOrEqualTo(maxResult);
        assertThat(rankedUsers).isSortedAccordingTo((u1, u2) -> u2.getWallet() - u1.getWallet());
    }

    @DisplayName("사용자를 collection point 기준으로 정렬한 뒤 maxResult 보다 작은 수의 사용자를 반환할 수 있다.")
    @ParameterizedTest
    @CsvSource({"0, 5", "1, 7", "1, 10"})
    void rankByCollectionPoint(int startPosition, int maxResult) {
        //given
        for (int i = 0; i < 17; i++) {
            User user = User.builder().intraId("user " + i).collectionPoint(i).build();
            userRepository.save(user);
        }

        UserRankQueryDto dto = new UserRankQueryDto(startPosition, maxResult);

        //when
        List<User> rankedUsers = userRepository.getRankByCollectionPoint(dto);

        rankedUsers.stream()
                .forEach(u -> log.info("collection point = {}", u.getCollectionPoint()));

        //then
        assertThat(rankedUsers.size()).isLessThanOrEqualTo(maxResult);
        assertThat(rankedUsers).isSortedAccordingTo((u1, u2) -> u2.getCollectionPoint() - u1.getCollectionPoint());
    }

    @DisplayName("사용자의 intraId 로 사용자를 조회할 수 있다.")
    @Test
    void findByIntraId() {
        //given
        User user1 = User.builder()
                .intraId("test1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .intraId("test2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        //when
        User findUser = userRepository.findByIntraId("test1").get();

        //then
        assertThat(findUser).isNotNull();
        assertThat(findUser.getId()).isEqualTo(user1.getId());
    }

}