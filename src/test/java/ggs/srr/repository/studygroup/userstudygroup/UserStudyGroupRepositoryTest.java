package ggs.srr.repository.studygroup.userstudygroup;

import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.userstudygroup.UserStudyGroup;
import ggs.srr.domain.user.User;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import ggs.srr.repository.studygroup.studygroup.StudyGroupRepository;
import ggs.srr.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserStudyGroupRepositoryTest {

    @Autowired
    UserStudyGroupRepository userStudyGroupRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyGroupRepository studyGroupRepository;
    @Autowired
    private RestClient.Builder builder;

    @DisplayName("사용자 스터디그룹을 저장 및 id 로 조회할 수 있다.")
    @Test
    void findById() {
        //given
        User user = User.builder().build();
        userRepository.save(user);

        StudyGroup studyGroup = new StudyGroup("test group");
        studyGroupRepository.save(studyGroup);

        UserStudyGroup userStudyGroup = new UserStudyGroup();
        userStudyGroup.registerGroup(studyGroup);
        userStudyGroup.registerUser(user);
        userStudyGroupRepository.save(userStudyGroup);

        //when
        UserStudyGroup findUserStudyGroup = userStudyGroupRepository.findById(userStudyGroup.getId());

        //then
        assertThat(findUserStudyGroup).isSameAs(userStudyGroup);
        assertThat(findUserStudyGroup.getUser()).isSameAs(user);
        assertThat(findUserStudyGroup.getStudyGroup()).isSameAs(studyGroup);

    }

    @DisplayName("사용자 스터디 그룹을 Null 로 조회시 예외가 발생한다.")
    @Test
    void findByNull() {
        assertThatThrownBy(() -> userStudyGroupRepository.findById(null))
                .isInstanceOf(FindIdNullException.class);
    }

    @DisplayName("사용자가 자신의 스터디 그룹을 조회할 경우 모두 조회되어야 한다.")
    @Test
    void findByUserId() {
        User user = User.builder().build();
        userRepository.save(user);

        StudyGroup studyGroup1 = new StudyGroup("test1");
        StudyGroup studyGroup2 = new StudyGroup("test2");

        studyGroupRepository.save(studyGroup1);
        studyGroupRepository.save(studyGroup2);

        UserStudyGroup userStudyGroup1 = new UserStudyGroup();
        UserStudyGroup userStudyGroup2 = new UserStudyGroup();

        userStudyGroup1.registerUser(user);
        userStudyGroup2.registerUser(user);

        userStudyGroup1.registerGroup(studyGroup1);
        userStudyGroup2.registerGroup(studyGroup2);

        userStudyGroupRepository.save(userStudyGroup1);
        userStudyGroupRepository.save(userStudyGroup2);

        List<UserStudyGroup> userStudyGroups = userStudyGroupRepository.findByUserId(user.getId());

        assertThat(userStudyGroups).hasSize(2)
                .extracting("user", "studyGroup")
                .containsExactlyInAnyOrder(
                        tuple(user, studyGroup1),
                        tuple(user, studyGroup2)
                );

    }

    @DisplayName("사용자의 스터디 그룹 조회시 null 을 입력할 수 없다.")
    @Test
    void findByNullUserId() {
        assertThatThrownBy(() -> userStudyGroupRepository.findByUserId(null))
                .isInstanceOf(FindIdNullException.class);
    }
}