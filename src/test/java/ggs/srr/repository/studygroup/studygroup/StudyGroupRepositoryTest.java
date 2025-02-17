package ggs.srr.repository.studygroup.studygroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StudyGroupRepositoryTest {

    @Autowired
    StudyGroupRepository studyGroupRepository;

    @DisplayName("스터디 그룹을 저장할 경우 id 로 조회가능해야 한다.")
    @Test
    void findById() {
        StudyGroup studyGroup = new StudyGroup("test group");

        studyGroupRepository.save(studyGroup);
        Long studyGroupId = studyGroup.getId();
        StudyGroup findStudyGroup = studyGroupRepository.findById(studyGroupId);

        assertThat(findStudyGroup).isSameAs(studyGroup);
    }

    @DisplayName("그룹 id 로 조회시 null 이 입력될 경우 예외가 발생한다.")
    @Test
    void findByNull() {

        assertThatThrownBy(() -> studyGroupRepository.findById(null))
                .isInstanceOf(FindIdNullException.class);

    }
}