package ggs.srr.repository.reservation.studygroup;

import ggs.srr.domain.reservation.studygroup.StudyGroup;
import ggs.srr.repository.reservation.exception.group.NullGroupIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class GroupRepositoryTest {

    @Autowired StudyGroupRepository studyGroupRepository;

    @DisplayName("스터디 그룹을 저장할 경우 id 로 조회가능해야 한다.")
    @Test
    void findById() {
        StudyGroup studyGroup = new StudyGroup();

        studyGroupRepository.save(studyGroup);
        Long studyGroupId = studyGroup.getId();
        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId);

        assertThat(findStudyGroup).isSameAs(studyGroup);
    }

    @DisplayName("그룹 id 로 조회시 null 이 입력될 경우 예외가 발생한다.")
    @Test
    void findByNull() {

        assertThatThrownBy(() -> studyGroupRepository.findById(null))
                .isInstanceOf(NullGroupIdException.class);

    }
}