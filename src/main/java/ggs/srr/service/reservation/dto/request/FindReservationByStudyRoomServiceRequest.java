package ggs.srr.service.reservation.dto.request;

import lombok.Data;

@Data
public class FindReservationByStudyRoomServiceRequest {
    private Long studyRoomId;

    public FindReservationByStudyRoomServiceRequest(Long studyRoomId) {
        this.studyRoomId = studyRoomId;
    }
}
