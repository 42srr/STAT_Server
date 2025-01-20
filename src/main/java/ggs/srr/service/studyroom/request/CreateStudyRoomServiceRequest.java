package ggs.srr.service.studyroom.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @Builder
    private CreateStudyRoomServiceRequest(long studyRoomId, String name, String img, LocalDateTime openTime, LocalDateTime closeTime,
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
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
