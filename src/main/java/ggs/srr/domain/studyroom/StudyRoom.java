package ggs.srr.domain.studyroom;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "study_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_room_id")
    private Long id;

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
