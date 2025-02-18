package ggs.srr.repository.reservation;


import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import ggs.srr.repository.studygroup.studygroup.StudyGroupRepository;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    @Autowired
    private EntityManager em;

    @DisplayName("예약을 저장할 경우 스터디룸 id로 조회가능 해야 한다.")
    @Test
    void findByStudyRoomId() {
        StudyRoom studyRoom = new StudyRoom("오아시스", "test.com", null, null, 1, 1, 0, 0);
        StudyGroup studyGroup = new StudyGroup("test group");
        studyRoomRepository.save(studyRoom);
        studyGroupRepository.save(studyGroup);

        Reservation reservation = new Reservation();
        reservation.initializeStudyRoom(studyRoom);
        reservation.initializeStudyGroup(studyGroup);

        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findByStudyRoomId(studyRoom.getId());
        assertThat(reservations.get(0).getStudyRoom()).isSameAs(studyRoom);
    }

    @DisplayName("예약을 저장할 경우 스터디그룹 id로 조회가능 해야 한다.")
    @Test
    void findByStudyGroupId() {
        StudyRoom studyRoom = new StudyRoom("오아시스", "test.com", null, null, 1, 1, 0, 0);
        StudyGroup studyGroup = new StudyGroup("test group");
        studyRoomRepository.save(studyRoom);
        studyGroupRepository.save(studyGroup);

        Reservation reservation = new Reservation();
        reservation.initializeStudyRoom(studyRoom);
        reservation.initializeStudyGroup(studyGroup);

        reservationRepository.save(reservation);

        List<Reservation> reservations = reservationRepository.findByStudyGroupId(studyGroup.getId());
        assertThat(reservations.get(0).getStudyGroup()).isSameAs(studyGroup);

    }

    @DisplayName("예약 id로 조회시 null은 예외이다.")
    @Test
    void findByNull() {
        assertThatThrownBy(() -> studyRoomRepository.findById(null))
                .isInstanceOf(FindIdNullException.class);
    }

    @DisplayName("예약 시작 시간과 종료 시간으로 조회시 해당 되는 예약이 있을 경우 해당되는 모든 예약을 반환해야 한다.")
    @Test
    void findByTimeForUpdate() {
        //given
        LocalDate date = LocalDate.of(2025, 2, 10);

        List<StudyRoom> studyRooms = createStudyRoom();
        studyRooms.forEach(studyRoomRepository::save);

        List<Reservation> reservations = createReservations(date, studyRooms.get(0), studyRooms.get(1));
        reservations.forEach(reservationRepository::save);

        em.flush();
        em.clear();

        LocalDateTime startDateTime = LocalDateTime.of(date, LocalTime.of(12, 0));
        LocalDateTime ebdDateTime = LocalDateTime.of(date, LocalTime.of(18, 0));
        StudyRoom wantedStudyRoom1 = studyRooms.get(0);
        StudyRoom wantedStudyRoom2 = studyRooms.get(1);

        //when
        List<Reservation> findReservations1 = reservationRepository.findByTimeForUpdate(startDateTime, ebdDateTime, wantedStudyRoom1);
        List<Reservation> findReservations2 = reservationRepository.findByTimeForUpdate(startDateTime, ebdDateTime, wantedStudyRoom2);


        //then
        assertThat(findReservations1.size()).isEqualTo(3);
        assertThat(findReservations2.size()).isEqualTo(2);
    }

    /*
        todo
        쭝구리가 내일 해야할 일
        1. 기존 예약 조회시 스터디룸 아이디 또한 파라미터로 넘기게 변경해야 한다잇
        2. 그에 맞는 테스트 코드도 짜야 한다잇
        3. 동시성 테스트 까지 완료 해야 한다잇
        4. 나머지는 노가다로 클리어 해야 한다잇

    */

    private List<StudyRoom> createStudyRoom() {
        StudyRoom s1 = new StudyRoom("s1", null, LocalTime.of(9, 0), LocalTime.of(18, 0), 0, 0,0,0);
        StudyRoom s2 = new StudyRoom("s2", null, LocalTime.of(9, 0), LocalTime.of(18, 0), 0, 0,0,0);
        return List.of(s1, s2);
    }

    private List<Reservation> createReservations(LocalDate date, StudyRoom s1, StudyRoom s2) {
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();
        Reservation r3 = new Reservation();
        Reservation r4 = new Reservation();
        Reservation r5 = new Reservation();
        Reservation r6 = new Reservation();

        r1.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(10, 0)), LocalDateTime.of(date, LocalTime.of(12, 0)));
        r2.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(12, 0)), LocalDateTime.of(date, LocalTime.of(14, 0)));
        r3.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(14, 0)), LocalDateTime.of(date, LocalTime.of(16, 0)));
        r4.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(16, 0)), LocalDateTime.of(date, LocalTime.of(18, 0)));
        r5.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(12, 0)), LocalDateTime.of(date, LocalTime.of(18, 0)));
        r6.initializeReservationDateTime(LocalDateTime.of(date, LocalTime.of(14, 0)), LocalDateTime.of(date, LocalTime.of(18, 0)));

        r1.initializeStudyRoom(s1);
        r2.initializeStudyRoom(s1);
        r3.initializeStudyRoom(s1);
        r4.initializeStudyRoom(s1);
        r5.initializeStudyRoom(s2);
        r6.initializeStudyRoom(s2);

        return List.of(r1, r2, r3, r4, r5, r6);

    }
}

