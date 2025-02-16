package ggs.srr.repository.reservation;


import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import ggs.srr.repository.studygroup.studygroup.StudyGroupRepository;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private StudyRoomRepository studyRoomRepository;
    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @DisplayName("예약을 저장할 경우 스터디룸 id로 조회가능 해야 한다.")
    @Test
    void findByStudyRoomId() {
        StudyRoom studyRoom = new StudyRoom("오아시스", "test.com", null, null, 1, 1, 0, 0);
        StudyGroup studyGroup = new StudyGroup("test group");
        studyRoomRepository.save(studyRoom);
        studyGroupRepository.save(studyGroup);

        Reservation reservation = new Reservation();
        reservation.setStudyRoom(studyRoom);
        reservation.setStudyGroup(studyGroup);

        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findByStudyRoomId(studyRoom.getId());
        assertThat(reservations.get(0).getStudyRoom().getId()).isSameAs(studyRoom.getId());
    }

    @DisplayName("예약을 저장할 경우 스터디그룹 id로 조회가능 해야 한다.")
    @Test
    void findByStudyGroupId() {
        StudyRoom studyRoom = new StudyRoom("오아시스", "test.com", null, null, 1, 1, 0, 0);
        StudyGroup studyGroup = new StudyGroup("test group");
        studyRoomRepository.save(studyRoom);
        studyGroupRepository.save(studyGroup);

        Reservation reservation = new Reservation();
        reservation.setStudyRoom(studyRoom);
        reservation.setStudyGroup(studyGroup);

        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findByStudyGroupId(studyGroup.getId());
        assertThat(reservations.get(0).getStudyGroup().getId()).isSameAs(studyGroup.getId());

    }

    @DisplayName("예약 id로 조회시 null은 예외이다.")
    @Test
    void findByNull() {
        assertThatThrownBy(() -> studyRoomRepository.findById(null))
                .isInstanceOf(FindIdNullException.class);
    }
}

