package ggs.srr.service.reservation.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class FindReservationsResponse {

    List<ReservationResponse> reservations;

    public FindReservationsResponse(List<ReservationResponse> reservations) {
        this.reservations = reservations;
    }
}
