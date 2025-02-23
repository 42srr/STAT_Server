package ggs.srr.service.reservation.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateReservationServiceRequest {

    private Long studyGroupId;
    private Long studyRoomId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

}
