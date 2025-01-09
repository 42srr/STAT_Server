package ggs.srr.repository.reservation.userstudygroup;

import ggs.srr.domain.reservation.studygroup.StudyGroup;
import ggs.srr.domain.reservation.userstudygroup.UserStudyGroup;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.reservation.exception.FindIdNullException;
import ggs.srr.repository.reservation.studygroup.StudyGroupRepository;
import ggs.srr.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("사용자 스터디그룹을 저장 및 id 로 조회할 수 있다.")
    @Test
    void findById() {
        //given
        FtUser ftUser = new FtUser();
        userRepository.save(ftUser);

        StudyGroup studyGroup = new StudyGroup();
        studyGroupRepository.save(studyGroup);

        UserStudyGroup userStudyGroup = new UserStudyGroup();
        userStudyGroup.registerGroup(studyGroup);
        userStudyGroup.registerUser(ftUser);
        userStudyGroupRepository.save(userStudyGroup);

        //when
        UserStudyGroup findUserStudyGroup = userStudyGroupRepository.findById(userStudyGroup.getId());

        //then
        assertThat(findUserStudyGroup).isSameAs(userStudyGroup);
        assertThat(findUserStudyGroup.getUser()).isSameAs(ftUser);
        assertThat(findUserStudyGroup.getStudyGroup()).isSameAs(studyGroup);

    }

    @DisplayName("사용자 스터디 그룹을 Null 로 조회시 예외가 발생한다.")
    @Test
    void findByNull() {
        assertThatThrownBy(() -> userStudyGroupRepository.findById(null))
                .isInstanceOf(FindIdNullException.class);
    }
}