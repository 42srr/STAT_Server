package ggs.srr.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import ggs.srr.domain.user.FtUser;
import ggs.srr.domain.user.Role;
import ggs.srr.repository.user.UserRepository;
import jakarta.transaction.Transactional;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @DisplayName("level이 올바르게 입력되는지 확인한다.")
    @Test
    void CheckLevelCorrectly() {
        saveUser(4, List.of(1.0, 3.2, 2.3, 1.7));

        LevelResponseList usersLevelInfo = userService.getLevelInfo();
        List<LevelResponse> levelResponseList = usersLevelInfo.getLevelResponseList();

        assertThat(levelResponseList.get(1).getCount()).isEqualTo(2);
        assertThat(levelResponseList.get(2).getCount()).isEqualTo(1);
        assertThat(levelResponseList.get(3).getCount()).isEqualTo(1);
    }

    @DisplayName("level이 21보다 큰 데이터가 있을 경우, 리스트의 사이즈가 해당 숫자에 맞게 확장된다.")
    @Test
    void CheckLevelOver21() {
        // Given
        saveUser(3, List.of(21.0, 22.7, 24.4));
        // When
        LevelResponseList usersLevelInfo = userService.getLevelInfo();
        List<LevelResponse> levelResponseList = usersLevelInfo.getLevelResponseList();
        // Then
        assertThat(levelResponseList.get(21).getCount()).isEqualTo(1);
        assertThat(levelResponseList.get(22).getCount()).isEqualTo(1);
        assertThat(levelResponseList.get(23).getCount()).isEqualTo(0);
        assertThat(levelResponseList.get(24).getCount()).isEqualTo(1);
        assertThat(levelResponseList.size()).isEqualTo(25);
    }

    private void saveUser(int size, List<Double> levels) {
        for (int i = 0; i < size; i++) {
            FtUser user = new FtUser(1, "1", Role.CADET, 0, 0, levels.get(i), null);
            userRepository.save(user);
        }
    }
}