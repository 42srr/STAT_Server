package ggs.srr.domain.reservation;

import ggs.srr.domain.reservation.exception.InvalidReservationTimeException;
import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.studyroom.StudyRoom;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    private StudyRoom studyRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Reservation() {
    }

    public void initializeStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;

        //무한루프 방지
        if (!studyRoom.getReservations().contains(this)) {
            studyRoom.getReservations().add(this);
        }
    }

    public void updateStudyRoom(StudyRoom studyRoom) {
        this.studyRoom = studyRoom;
        studyRoom.getReservations().add(this);
    }

    public void initializeStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;

        //무한루프 방지
        if (!studyGroup.getReservations().contains(this)) {
            studyGroup.getReservations().add(this);
        }
    }

    public void initializeReservationDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime)) {
            throw new InvalidReservationTimeException("예약 시작 시간은 항상 예약 종료 시간 보다 앞서야 합니다.");
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public void updateReservationDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime.isAfter(endDateTime) || startDateTime.equals(endDateTime)) {
            throw new InvalidReservationTimeException("예약 시작 시간은 항상 예약 종료 시간 보다 앞서야 합니다.");
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }
}
