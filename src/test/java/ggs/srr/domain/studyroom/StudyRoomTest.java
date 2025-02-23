package ggs.srr.domain.studyroom;

import ggs.srr.domain.studyroom.exception.InvalidRequestTimeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


class StudyRoomTest {

    @DisplayName("스터디룸 운영시간이 아닌 경우 검증 시도시 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {"8, 10", "8, 19", "17, 19"})
    void invalidOperationTime(int start, int end) {
        //given
        LocalTime startTime = LocalTime.of(start, 0, 0);
        LocalTime endTime = LocalTime.of(end, 0, 0);
        StudyRoom studyRoom = new StudyRoom("test_study_room", null, LocalTime.of(9, 0, 0), LocalTime.of(18, 0, 0), 1, 1, 1, 1);

        //when //then
        assertThatThrownBy(() -> studyRoom.validateReservationTime(startTime, endTime))
                .isInstanceOf(InvalidRequestTimeException.class);
    }
}