package ggs.srr.domain.reservation;

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
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_room_id")
    private StudyRoom studyRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id")
    private StudyGroup studyGroup;

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

    public void initializeStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;

        //무한루프 방지
        if (!studyGroup.getReservations().contains(this)) {
            studyGroup.getReservations().add(this);
        }
    }
}
