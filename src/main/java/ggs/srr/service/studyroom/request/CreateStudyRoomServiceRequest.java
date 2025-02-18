package ggs.srr.service.studyroom.request;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class CreateStudyRoomServiceRequest {
    long studyRoomId;
    String name;
    String img;
    LocalTime openTime;
    LocalTime closeTime;
    int open24Flag;
    int drinkFlag;
    int eatFlag;
    int useToolFlag;
    LocalDateTime createdAt;
    LocalDateTime modifiedAt;

    @Builder
    private CreateStudyRoomServiceRequest(long studyRoomId, String name, String img, LocalTime openTime, LocalTime closeTime,
                                         int option) {
        this.studyRoomId = studyRoomId;
        this.name = name;
        this.img = img;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.open24Flag = option & 1;
        this.drinkFlag = (option >> 1) & 1;
        this.eatFlag =  (option >> 2) & 1;
        this.useToolFlag =  (option >> 3) & 1;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
}
