package ggs.srr.api.controller.bocal.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class StudyRoomDeletionRequest {
    Long studyRoomId;
    String name;

    @Builder
    private StudyRoomDeletionRequest(Long studyRoomId, String name) {
        this.studyRoomId = studyRoomId;
        this.name = name;
    }

}
