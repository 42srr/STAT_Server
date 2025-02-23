package ggs.srr.domain.studyroom;

import ggs.srr.domain.reservation.Reservation;
import ggs.srr.domain.studyroom.exception.InvalidRequestTimeException;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "study_room")
public class StudyRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_id")
    private Long id;

    @OneToMany(mappedBy = "studyRoom")
    private List<Reservation> reservations = new ArrayList<>();

    private String name;
    private String img;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int isOpen24Hour;
    private int canDrink;
    private int canEat;
    private int canUseTool;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);

        //무한루프 방지
        if (reservation.getStudyRoom() != this) {
            reservation.initializeStudyRoom(this);
        }
    }

    protected StudyRoom() {
    }

    public StudyRoom(String name, String img,
                     LocalTime openTime, LocalTime closeTime,
                     int isOpen24Hour, int canDrink
    ,               int canEat, int canUseTool) {
        this.name = name;
        this.img = img;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen24Hour = isOpen24Hour;
        this.canDrink = canDrink;
        this.canEat = canEat;
        this.canUseTool = canUseTool;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    public void validateReservationTime(LocalTime startTime, LocalTime endTime) {

        if (openTime.isAfter(startTime) || closeTime.isBefore(endTime)) {
            throw new InvalidRequestTimeException("해당 시간대에는 예약할 수 없습니다.");
        }
    }

}
