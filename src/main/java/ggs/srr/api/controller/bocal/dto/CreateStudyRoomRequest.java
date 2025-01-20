package ggs.srr.api.controller.bocal.dto;

import ggs.srr.service.studyroom.dto.CreateStudyRoomServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CreateStudyRoomRequest {
    @NotNull(message = "스터디룸 id는 필수입니다.")
    Long studyRoomId;

    @NotBlank(message = "스터디 룸 이름은 필수입니다.")
    String name;

    @NotBlank(message = "스터디 룸 사진은 필수입니다.")
    String img;

    @NotNull(message = "스터디룸 오픈 시간은 필수입니다.")
    LocalDateTime openTime;

    @NotNull(message = "스터디룸 닫는 시간은 필수입니다.")
    LocalDateTime closeTime;

    @NotNull(message = "스터디룸 24시간 개방 여부는 필수입니다.")
    Boolean isOpen24Hour;

    @NotNull(message = "스터디룸 음료 섭취 가능 여부는 필수입니다.")
    Boolean canDrink;

    @NotNull(message = "스터디룸 음식 섭취 가능 여부는 필수입니다.")
    Boolean canEat;

    @NotNull(message = "스터디룸 기자재 사용가능 여부는 필수입니다.")
    Boolean canUseTool;

    public CreateStudyRoomServiceRequest requestStudyRoomtoStudyRoom() {
        return new CreateStudyRoomServiceRequest(studyRoomId, name, img, openTime, closeTime, isOpen24Hour, canDrink, canEat, canUseTool);
    }
}
