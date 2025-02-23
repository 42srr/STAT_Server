package ggs.srr.repository.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class StudyRoomRepositoryTest {

    @Autowired
    StudyRoomRepository repository;

    @DisplayName("스터디룸을 저장 및 조회할 수 있어야 한다.")
    @Test
    void save() {
        //given
        StudyRoom studyRoom = new StudyRoom("test", "image", null, null, 0, 0, 0, 0);
        repository.save(studyRoom);

        //when
        StudyRoom findStudyRoom = repository.findById(studyRoom.getId());

        //then
        assertThat(findStudyRoom).isNotNull();
        assertThat(findStudyRoom.getName()).isEqualTo("test");
    }

}