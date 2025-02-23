package ggs.srr.service.reservation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateReservationServiceRequest {

    private Long requestStudyGroupId;
    private Long reservationId;
    private Long studyRoomId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
