package ggs.srr.api.controller.bocal.dto;

import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
public class StudyRoomCreationRequest {
    @NotNull(message = "스터디룸 id는 필수입니다.")
    Long studyRoomId;

    @NotBlank(message = "스터디 룸 이름은 필수입니다.")
    String name;

    @NotBlank(message = "스터디 룸 사진은 필수입니다.")
    String img;

    @NotNull(message = "스터디룸 오픈 시간은 필수입니다.")
    LocalTime openTime;

    @NotNull(message = "스터디룸 닫는 시간은 필수입니다.")
    LocalTime closeTime;

    @NotNull(message = "option은 널일 수 없습니다.")
    @Min(value = 0, message = "option은 최소 0부터 시작입니다.")
    @Max(value = 15, message = "option은 최대 15까지 입니다.")
    Integer option;

    public StudyRoomCreationRequest() {
    }

    @Builder
    private StudyRoomCreationRequest(LocalTime closeTime, String img, String name, LocalTime openTime, Integer option, Long studyRoomId) {
        this.closeTime = closeTime;
        this.img = img;
        this.name = name;
        this.openTime = openTime;
        this.option = option;
        this.studyRoomId = studyRoomId;
    }

    public CreateStudyRoomServiceRequest toServiceRequest() {
        return CreateStudyRoomServiceRequest.builder()
                .studyRoomId(studyRoomId)
                .name(name)
                .img(img)
                .openTime(openTime)
                .closeTime(closeTime)
                .option(option)
                .build();
    }
}
