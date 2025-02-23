package ggs.srr.service.reservation;

import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studyroom.exception.InvalidRequestTimeException;
import ggs.srr.repository.reservation.ReservationRepository;
import ggs.srr.service.reservation.dto.request.*;
import ggs.srr.service.reservation.dto.response.FindReservationsResponse;
import ggs.srr.service.reservation.exception.DuplicateReservationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * ReservationServiceTest 는 resource/reservation-test-sql/reservation-data.sql 로 데이터를 먼저 넣은 다음 실행하는 테스트입니다.
 * <p>
 * StudyGroup 생성 목록
 * test1 , test2, test3 미리 생성
 * <p>
 * StudyRoom 생성 목록
 * test_room_1 (개방시간 09, 운영 종료 시간  18)
 * <p>
 * 예약 목록
 * test_room_1 10 - 12 시 예약
 * test_room_1 16 - 18 시 예약
 */

@SpringBootTest
@Transactional
@Sql(scripts = "/reservation-test-sql/reservation-data.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED))
@ActiveProfiles("test")
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    ReservationRepository reservationRepository;

    @DisplayName("빈 시간대에 예약을 할 수 있다.")
    @Test
    void createReservation() {

        //given
        CreateReservationServiceRequest request = createRequest(1L, 1L, 12, 14);
        reservationService.createReservation(request);

        //when
        List<Reservation> findReservationByGroupId = reservationRepository.findByStudyGroupId(1L);
        List<Reservation> findReservationByStudyRoomId = reservationRepository.findByStudyRoomId(1L);

        //then
        assertThat(findReservationByGroupId).hasSize(2);
        assertThat(findReservationByStudyRoomId).hasSize(4);
    }


    @DisplayName("스터디룸이 운영하는 시간 외에 예약을 시도할 경우 예외가 발생한다.")
    @Test
    void createReservationAtNotOperatingHour() {

        //given
        CreateReservationServiceRequest request = createRequest(1L, 1L, 8, 13);

        //when //then
        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(InvalidRequestTimeException.class);
    }

    @DisplayName("동시에 여러명이 겹치는 시간대에 예약할 경우 예약은 단 한건만 이루어 진다.")
    @Test
    void createReservationAtTheSameTime() throws InterruptedException {

        //given
        int clientCount = 3;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(clientCount);
        CountDownLatch latch = new CountDownLatch(clientCount);

        CreateReservationServiceRequest r1 = createRequest(1L, 1L, 12, 15);
        CreateReservationServiceRequest r2 = createRequest(2L, 1L, 12, 15);
        CreateReservationServiceRequest r3 = createRequest(2L, 1L, 12, 15);

        List<CreateReservationServiceRequest> requests = List.of(r1, r2, r3);

        //when
        for (CreateReservationServiceRequest request : requests) {
            es.execute(() -> {
                try {
                    reservationService.createReservation(request);
                    successCount.getAndIncrement();
                } catch (DuplicateReservationException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        assertThat(successCount.intValue()).isEqualTo(1);
        assertThat(failCount.intValue()).isEqualTo(2);

    }

    @DisplayName("동시에 여러명이 다른 시간대에 예약할 경우 모든 요청은 예약 가능하다.")
    @Test
    void createReservationAtTheOtherTime() throws InterruptedException {

        //given
        int clientCount = 3;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);
        ExecutorService es = Executors.newFixedThreadPool(clientCount);
        CountDownLatch latch = new CountDownLatch(clientCount);

        CreateReservationServiceRequest r1 = createRequest(1L, 1L, 12, 13);
        CreateReservationServiceRequest r2 = createRequest(2L, 1L, 13, 14);
        CreateReservationServiceRequest r3 = createRequest(2L, 1L, 14, 15);

        List<CreateReservationServiceRequest> requests = List.of(r1, r2, r3);
        System.out.println("requests = " + requests);

        //when
        for (CreateReservationServiceRequest request : requests) {
            es.execute(() -> {
                try {
                    reservationService.createReservation(request);
                    successCount.getAndIncrement();
                } catch (DuplicateReservationException e) {
                    failCount.getAndIncrement();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        assertThat(successCount.intValue()).isEqualTo(3);
        assertThat(failCount.intValue()).isEqualTo(0);

    }

    @DisplayName("예약을 사용자 그룹별로 조회할 수 있다.")
    @Test
    void findByStudyGroup() {
        //given
        CreateReservationServiceRequest request = createRequest(1L, 1L, 12, 14);
        reservationService.createReservation(request);

        //when
        FindReservationsResponse response = reservationService.findReservationsByStudyGroup(new FindReservationByStudyGroupServiceRequest(1L));

        //then
        assertThat(response.getReservations()).hasSize(2);
    }


    @DisplayName("예약을 사용자 그룹별로 조회할 수 있다.")
    @Test
    void findByStudyRoom() {
        //given
        CreateReservationServiceRequest request = createRequest(1L, 1L, 12, 14);
        reservationService.createReservation(request);

        //when
        FindReservationsResponse response = reservationService.findReservationsByStudyRoom(new FindReservationByStudyRoomServiceRequest(1L));

        //then
        assertThat(response.getReservations()).hasSize(4);
    }


    @DisplayName("예약을 취소할 수 있다.")
    @Test
    void deleteReservation() {
        //given
        // db 에 pk 가 -1 인 reservation 이 있음
        DeleteReservationServiceRequest request = new DeleteReservationServiceRequest(-1L);
        reservationService.deleteReservation(request);

        //when
        FindReservationsResponse response = reservationService.findReservationsByStudyRoom(new FindReservationByStudyRoomServiceRequest(1L));

        //then
        assertThat(response.getReservations()).hasSize(2);
    }


    @DisplayName("예약 시간대 변경시 특정 스터디룸에 기존 예약이 없다면 시간대 변경이 가능하다")
    @Test
    void updateReservation() {
        //given
        UpdateReservationServiceRequest request = new UpdateReservationServiceRequest();
        request.setReservationId(-3L);
        request.setRequestStudyGroupId(1L);
        request.setStudyRoomId(2L);
        request.setStartDateTime(LocalDateTime.of(2025, 2, 10, 10, 0, 0));
        request.setEndDateTime(LocalDateTime.of(2025, 2, 10, 12, 0, 0));

        reservationService.updateReservation(request);
        //when
        FindReservationsResponse findByStudyRoom2 = reservationService.findReservationsByStudyRoom(new FindReservationByStudyRoomServiceRequest(2L));
        FindReservationsResponse findByStudyRoom1 = reservationService.findReservationsByStudyRoom(new FindReservationByStudyRoomServiceRequest(1L));
        FindReservationsResponse findByStudyGroup = reservationService.findReservationsByStudyGroup(new FindReservationByStudyGroupServiceRequest(1L));
        //then
        assertThat(findByStudyRoom2.getReservations()).hasSize(1);
        assertThat(findByStudyRoom1.getReservations()).hasSize(2);
        assertThat(findByStudyGroup.getReservations()).hasSize(1);

    }

    @DisplayName("예약 시간대 변경시 특정 스터디룸에 기존 예약이 있을 경우 예외가 발생한다.")
    @Test
    void updateReservationWithWrongTim() {
        UpdateReservationServiceRequest request = new UpdateReservationServiceRequest();
        request.setReservationId(-3L);
        request.setRequestStudyGroupId(1L);
        request.setStudyRoomId(1L);
        request.setStartDateTime(LocalDateTime.of(2025, 2, 10, 16, 0, 0));
        request.setEndDateTime(LocalDateTime.of(2025, 2, 10, 18, 0, 0));

        assertThatThrownBy(() -> reservationService.updateReservation(request))
                .isInstanceOf(DuplicateReservationException.class);
    }

    @DisplayName("예약 시간대 변경시 스터디룸 운영 시간이 아닐 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {"8, 10", "8, 19", "16, 19"})
    void updateReservationWithNotOpenTime(int start, int end) {
        UpdateReservationServiceRequest request = new UpdateReservationServiceRequest();
        request.setReservationId(-3L);
        request.setRequestStudyGroupId(1L);
        request.setStudyRoomId(1L);
        request.setStartDateTime(LocalDateTime.of(2025, 2, 10, start, 0, 0));
        request.setEndDateTime(LocalDateTime.of(2025, 2, 10, end, 0, 0));

        assertThatThrownBy(() -> reservationService.updateReservation(request))
                .isInstanceOf(InvalidRequestTimeException.class);
    }

    private CreateReservationServiceRequest createRequest(Long studyGroupId, Long studyRoomId, int startTime, int endTime) {

        CreateReservationServiceRequest request = new CreateReservationServiceRequest();
        request.setStudyGroupId(studyGroupId);
        request.setStudyRoomId(studyRoomId);
        request.setStartDateTime(createLocalDateTime(startTime));
        request.setEndDateTime(createLocalDateTime(endTime));

        return request;
    }

    private LocalDateTime createLocalDateTime(int hour) {
        LocalTime localTime = LocalTime.of(hour, 0, 0);
        LocalDate localDate = LocalDate.of(2025, 2, 10);
        return LocalDateTime.of(localDate, localTime);
    }

}