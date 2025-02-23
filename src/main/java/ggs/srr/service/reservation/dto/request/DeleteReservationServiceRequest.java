package ggs.srr.service.reservation.dto.request;

import lombok.Data;

@Data
public class DeleteReservationServiceRequest {
    private Long reservationId;

    public DeleteReservationServiceRequest(Long reservationId) {
        this.reservationId = reservationId;
    }
}
