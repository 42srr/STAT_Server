package ggs.srr.api.controller.bocal.dto;

import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeleteStudyRoomRequest {
    Long studyRoomId;
    String name;

    @Builder
    private DeleteStudyRoomRequest(Long studyRoomId, String name) {
        this.studyRoomId = studyRoomId;
        this.name = name;
    }
    
}
