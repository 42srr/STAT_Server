package ggs.srr.service.reservation.dto.request;

import lombok.Data;

@Data
public class FindReservationByStudyGroupServiceRequest {
    Long studyGroupId;

    public FindReservationByStudyGroupServiceRequest(Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }
}
