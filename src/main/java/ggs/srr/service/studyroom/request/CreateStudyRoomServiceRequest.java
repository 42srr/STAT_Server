package ggs.srr.service.studyroom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CreateStudyRoomServiceRequest {
    long studyRoomId;
    String name;
    String img;
    LocalDateTime openTime;
    LocalDateTime closeTime;
    int isOpen24Hour;
    int canDrink;
    int canEat;
    int canUseTool;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    public CreateStudyRoomServiceRequest(long studyRoomId, String name, String img, LocalDateTime openTime, LocalDateTime closeTime,
                                         int option) {
        this.studyRoomId = studyRoomId;
        this.name = name;
        this.img = img;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isOpen24Hour = option & 1;
        this.canDrink = (option >> 1) & 1;
        this.canEat =  (option >> 2) & 1;
        this.canUseTool =  (option >> 3) & 1;
    }
}
