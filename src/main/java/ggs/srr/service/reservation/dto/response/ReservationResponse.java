package ggs.srr.service.reservation.dto.response;

import ggs.srr.domain.reservation.Reservation;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private String studyRoomName;
    private String groupName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public ReservationResponse(Reservation reservation) {
        studyRoomName = reservation.getStudyRoom().getName();
        groupName = reservation.getStudyGroup().getGroupName();
        startDateTime = reservation.getStartDateTime();
        endDateTime = reservation.getEndDateTime();
    }
}
