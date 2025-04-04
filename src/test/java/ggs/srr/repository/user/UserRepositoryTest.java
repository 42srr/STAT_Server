package ggs.srr.repository.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.common.FindByNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
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

}