package ggs.srr.domain.studyroom;

import ggs.srr.domain.reservation.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
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
                     LocalDateTime openTime, LocalDateTime closeTime,
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
}
