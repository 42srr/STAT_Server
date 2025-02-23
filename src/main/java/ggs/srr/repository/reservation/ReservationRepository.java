package ggs.srr.repository.reservation;

import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.reservation.exception.FindByNullException;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public List<Reservation> findByStudyRoomId(Long studyRoomId) {
        if (studyRoomId == null) {
            throw new FindIdNullException("예약 id로 예약 조회시 null을 전달할 없습니다.");
        }

        return em.createQuery("select r from Reservation r where r.studyRoom.id = :studyRoomId", Reservation.class)
                .setParameter("studyRoomId", studyRoomId)
                .getResultList();
    }

    public List<Reservation> findByStudyGroupId(Long studyGroupId) {
        if (studyGroupId == null) {
            throw new FindIdNullException("예약 id로 예약 조회시 null을 전달할 없습니다.");
        }

        return em.createQuery("select r from Reservation r where r.studyGroup.id = :studyGroupId", Reservation.class)
                .setParameter("studyGroupId", studyGroupId)
                .getResultList();
    }

    public Reservation findById(Long reservationId) {
        if (reservationId == null) {
            throw new FindIdNullException("예약 id로 예약 조회시 null을 전달할 없습니다.");
        }
        return em.find(Reservation.class, reservationId);
    }

    public void remove(Reservation reservation) {
        if (reservation != null) {
            em.remove(reservation);
        }
    }

    public List<Reservation> findByTimeForUpdate(LocalDateTime startDateTime, LocalDateTime endDateTime, StudyRoom studyRoom) {

        if (startDateTime == null || endDateTime == null || studyRoom == null) {
            throw new FindByNullException("null 을 이용해 해당 시간대의 예약 목록을 조회할 수 없습니다.");
        }
        return em.createQuery("select r from Reservation r where r.studyRoom = :studyRoom and r.startDateTime < :endDateTime and r.endDateTime > :startDateTime", Reservation.class)
                .setParameter("studyRoom", studyRoom)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList();
    }

}
