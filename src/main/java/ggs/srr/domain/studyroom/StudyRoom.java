package ggs.srr.domain.studyroom;

import ggs.srr.service.studyroom.dto.CreateStudyRoomServiceRequest;
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
    private Boolean isOpen24Hour;
    private Boolean canDrink;
    private Boolean canEat;
    private Boolean canUseTool;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public StudyRoom(String name, String img,
                     LocalDateTime openTime, LocalDateTime closeTime,
                     Boolean isOpen24Hour, Boolean canDrink
    ,               Boolean canEat, Boolean canUseTool) {
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
