package ggs.srr.api.controller.bocal;

import ggs.srr.api.ApiResponse;
import ggs.srr.api.controller.bocal.dto.CreateStudyRoomRequest;
import ggs.srr.service.studyroom.StudyRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StudyRoomController {
    private final StudyRoomService studyRoomService;

    @PostMapping("/studyroom")
    public ApiResponse<String> createRoom(@Valid @RequestBody CreateStudyRoomRequest createStudyRoomRequest) {
        studyRoomService.registerStudyroom(createStudyRoomRequest.requestStudyRoomtoStudyRoom());
        return ApiResponse.ok("Successfully created a study room");
    }
}
