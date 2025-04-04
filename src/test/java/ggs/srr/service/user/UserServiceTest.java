package ggs.srr.service.user;

import ggs.srr.domain.user.User;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.response.UserInformationResponse;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

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
}