package ggs.srr.service.studyroom.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateStudyRoomServiceRequest {
    long studyRoomId;
    String name;
    String img;
    LocalDateTime openTime;
    LocalDateTime closeTime;
    boolean isOpen24Hour;
    boolean canDrink;
    boolean canEat;
    boolean canUseTool;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public CreateStudyRoomServiceRequest(long studyRoomId, String name, String img, LocalDateTime openTime, LocalDateTime closeTime,
                                         boolean isOpen24Hour, boolean canDrink, boolean canEat, boolean canUseTool) {
        this.studyRoomId = studyRoomId;
        this.name = name;
        this.img = img;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen24Hour = isOpen24Hour;
        this.canDrink = canDrink;
        this.canEat = canEat;
        this.canUseTool = canUseTool;
    }
}
