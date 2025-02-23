package ggs.srr.service.reservation;

import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.reservation.ReservationRepository;
import ggs.srr.repository.studygroup.studygroup.StudyGroupRepository;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import ggs.srr.service.reservation.dto.request.*;
import ggs.srr.service.reservation.dto.response.FindReservationsResponse;
import ggs.srr.service.reservation.dto.response.ReservationResponse;
import ggs.srr.service.reservation.exception.DuplicateReservationException;
import ggs.srr.service.reservation.exception.NotFoundReservationException;
import ggs.srr.service.reservation.exception.NotFoundStudyGroupException;
import ggs.srr.service.reservation.exception.NotFoundStudyRoomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final StudyRoomRepository studyRoomRepository;

    @Transactional
    public void createReservation(CreateReservationServiceRequest request) {

        StudyRoom studyRoom = studyRoomRepository.findByIdForUpdate(request.getStudyRoomId());

        if (studyRoom == null) {
            throw new NotFoundStudyRoomException("해당 스터디룸을 찾을 수 없습니다.");
        }

        StudyGroup studyGroup = studyGroupRepository.findByIdForUpdate(request.getStudyGroupId());
        if (studyGroup == null) {
            throw new NotFoundStudyGroupException("없는 스터디 그룹입니다.");
        }


        LocalDateTime startDateTime = request.getStartDateTime();
        LocalDateTime endDateTime = request.getEndDateTime();

        studyRoom.validateReservationTime(startDateTime.toLocalTime(), endDateTime.toLocalTime());


        List<Reservation> findReservations = reservationRepository.findByTimeForUpdate(startDateTime, endDateTime, studyRoom);
        log.info("reservations = {}", findReservations.size());
        if (isDuplicatedReservation(findReservations)) {
            throw new DuplicateReservationException("이미 예약 되어 있는 시간대 입니다. 다른 시간대를 선택해 주세요.");
        }

        Reservation reservation = createReservation(studyRoom, studyGroup, startDateTime, endDateTime);

        reservationRepository.save(reservation);

    }

    public FindReservationsResponse findReservationsByStudyGroup(FindReservationByStudyGroupServiceRequest request) {

        List<Reservation> findReservations = reservationRepository.findByStudyGroupId(request.getStudyGroupId());
        List<ReservationResponse> responses = findReservations.stream()
                .map(ReservationResponse::new)
                .toList();
        return new FindReservationsResponse(responses);

    }

    public FindReservationsResponse findReservationsByStudyRoom(FindReservationByStudyRoomServiceRequest request) {

        List<Reservation> findReservations = reservationRepository.findByStudyRoomId(request.getStudyRoomId());
        List<ReservationResponse> responses = findReservations.stream()
                .map(ReservationResponse::new)
                .toList();
        return new FindReservationsResponse(responses);

    }

    @Transactional
    public void deleteReservation(DeleteReservationServiceRequest request) {
        Reservation findReservation = reservationRepository.findById(request.getReservationId());
        if (findReservation == null) {
            throw new NotFoundReservationException("해당 예약을 조회할 수 없습니다.");
        }

        reservationRepository.remove(findReservation);
    }


    @Transactional
    public void updateReservation(UpdateReservationServiceRequest request) {

        StudyRoom findStudyRoom = studyRoomRepository.findById(request.getStudyRoomId());
        if (findStudyRoom == null) {
            throw new NotFoundStudyRoomException("해당 스터디룸을 찾을 수 없습니다.");
        }

        Reservation findReservation = reservationRepository.findById(request.getReservationId());
        if (findReservation == null) {
            throw new NotFoundReservationException("해당 예약을 조회할 수 없습니다.");
        }

        LocalDateTime startDateTime = request.getStartDateTime();
        LocalDateTime endDateTime = request.getEndDateTime();

        findStudyRoom.validateReservationTime(startDateTime.toLocalTime(), endDateTime.toLocalTime());

        List<Reservation> reservations = reservationRepository.findByTimeForUpdate(startDateTime, endDateTime, findStudyRoom);
        if (isDuplicatedReservation(reservations)) {
            throw new DuplicateReservationException("이미 예약 되어 있는 시간대 입니다. 다른 시간대를 선택해 주세요.");
        }

        findReservation.updateReservationDateTime(startDateTime, endDateTime);
        findReservation.updateStudyRoom(findStudyRoom);
    }


    private Reservation createReservation(StudyRoom studyRoom, StudyGroup studyGroup, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Reservation reservation = new Reservation();
        reservation.initializeStudyRoom(studyRoom);
        reservation.initializeStudyGroup(studyGroup);
        reservation.initializeReservationDateTime(startDateTime, endDateTime);
        return reservation;
    }

    private boolean isDuplicatedReservation(List<Reservation> reservations) {
        return !reservations.isEmpty();
    }

}
