package ggs.srr.api.controller.studtygroup;

import ggs.srr.api.ApiResponse;
import ggs.srr.api.controller.studtygroup.request.StudyGroupCreateRequest;
import ggs.srr.api.controller.studtygroup.request.UserAllStudyGroupRequest;
import ggs.srr.service.reservation.exception.EmptyUserIdException;
import ggs.srr.service.reservation.studygroup.StudyGroupService;
import ggs.srr.service.reservation.studygroup.response.UserAllStudyGroupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @PostMapping("/study_groups")
    public ApiResponse<Object> createGroup(@Valid @RequestBody StudyGroupCreateRequest request) {

        log.info("userIds = {}", request.getUserIds());

        if (request.getUserIds().isEmpty()) {
            throw new EmptyUserIdException("사용자 id가 비어있을 수 없습니다.");
        }
        studyGroupService.createStudyGroup(request.toServiceRequest());

        return ApiResponse.ok(null);
    }

    @GetMapping("/study_groups/{userId}")
    public ApiResponse<List<UserAllStudyGroupResponse>> getUserStudyGroups(@PathVariable(name = "userId") Long userId) {

        UserAllStudyGroupRequest request = new UserAllStudyGroupRequest(userId);
        List<UserAllStudyGroupResponse> response = studyGroupService.getUsersStudyGroupResponse(request.toServiceRequest());

        return ApiResponse.ok(response);
    }
}
