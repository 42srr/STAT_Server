package ggs.srr.repository.reservation;

import ggs.srr.domain.reservation.Reservation;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReservationRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    //jpql
    /*
    @todo
     스터디룸 아이디 별 전체 예약 조회
     스터디 그롭 아이디 별 전체 예약 조회
     */
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


    public void delete(Long studyRoomId) {
        Reservation reservation = em.find(Reservation.class, studyRoomId);
        if (reservation != null) {
            em.remove(reservation);
        }
    }

    public List<Reservation> findByTimeForUpdate(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return em.createQuery("select r from Reservation r where r.startDateTime < :endDateTime and r.endDateTime > :startDateTime", Reservation.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}
