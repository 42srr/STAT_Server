package ggs.srr.domain.reservation;

import ggs.srr.domain.reservation.exception.InvalidReservationTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


class ReservationTest {

    @DisplayName("예약 시작 시간이 예약 종료 시간보다 앞설 경우 예약 가능하다.")
    @Test
    void initializeReservationDateTime() {
        //given
        Reservation reservation = new Reservation();
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 10, 12, 0, 0, 0);

        //when //then
        reservation.initializeReservationDateTime(startTime, endTime);

    }

    @DisplayName("예약 시작 시간이 예약 종료 시간보다 앞설 경우 예약 가능하다.")
    @Test
    void initializeReservationDateTimeWithInValidTime() {
        //given
        Reservation reservation = new Reservation();
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);

        //when //then
        assertThatThrownBy(() -> reservation.updateReservationDateTime(startTime, endTime))
                .isInstanceOf(InvalidReservationTimeException.class);

    }

    @DisplayName("예약 시작 시간이 예약 종료 시간보다 앞설 경우 예약 시간을 수정할 수 있다.")
    @Test
    void updateReservationTime() {
        Reservation reservation = new Reservation();
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 10, 12, 0, 0, 0);

        //when //then
        reservation.updateReservationDateTime(startTime, endTime);
    }

    @DisplayName("예약 시작 시간이 예약 종료 시간보다 앞설 경우 예약 시간을 수정할 수 있다.")
    @Test
    void updateReservationTimeWithInValidTime() {
        Reservation reservation = new Reservation();
        LocalDateTime startTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 2, 10, 10, 0, 0, 0);

        //when //then
        assertThatThrownBy(() -> reservation.updateReservationDateTime(startTime, endTime))
                .isInstanceOf(InvalidReservationTimeException.class);
    }
}