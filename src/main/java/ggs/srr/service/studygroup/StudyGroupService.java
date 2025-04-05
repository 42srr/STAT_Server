package ggs.srr.service.studygroup;

import ggs.srr.domain.studygroup.StudyGroup;
import ggs.srr.domain.userstudygroup.UserStudyGroup;
import ggs.srr.domain.user.User;
import ggs.srr.repository.studygroup.studygroup.StudyGroupRepository;
import ggs.srr.repository.studygroup.userstudygroup.UserStudyGroupRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.studygroup.exception.NoSuchUserException;
import ggs.srr.service.studygroup.request.StudyGroupCreateServiceRequest;
import ggs.srr.service.studygroup.request.UserAllStudyGroupServiceRequest;
import ggs.srr.service.studygroup.response.UserAllStudyGroupResponse;
import ggs.srr.service.studygroup.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyGroupService {

    private final UserRepository userRepository;
    private final UserStudyGroupRepository userStudyGroupRepository;
    private final StudyGroupRepository studyGroupRepository;

    @Transactional
    public void createStudyGroup(StudyGroupCreateServiceRequest request) {
        StudyGroup studyGroup = new StudyGroup(request.getGroupName());

        List<Long> userIds = request.getUserIds();

        for (Long userId : userIds) {
            User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException("존재하지 않는 회원입니다."));
            UserStudyGroup userStudyGroup = new UserStudyGroup();

            userStudyGroup.registerUser(user);
            userStudyGroup.registerGroup(studyGroup);

            userStudyGroupRepository.save(userStudyGroup);
        }

        studyGroupRepository.save(studyGroup);
    }

    public List<UserAllStudyGroupResponse> getUsersStudyGroupResponse(UserAllStudyGroupServiceRequest request) {
        Long userId = request.getUserId();

        return userStudyGroupRepository.findByUserId(userId).stream()
                .map(UserStudyGroup::getStudyGroup)
                .map(this::toAllStudyGroupResponse)
                .toList();

    }

    private UserAllStudyGroupResponse toAllStudyGroupResponse(StudyGroup studyGroup) {
        UserAllStudyGroupResponse userAllStudyGroupResponse = new UserAllStudyGroupResponse();

        userAllStudyGroupResponse.setGroupName(studyGroup.getGroupName());
        List<UserResponse> allUserResponse = getAllUserResponse(studyGroup);
        userAllStudyGroupResponse.setUsers(allUserResponse);
        userAllStudyGroupResponse.setSize(allUserResponse.size());

        return userAllStudyGroupResponse;
    }

    private List<UserResponse> getAllUserResponse(StudyGroup studyGroup) {
        return studyGroup.getUserStudyGroups().stream()
                .map(UserStudyGroup::getUser)
                .map(this::toUserResponse)
                .toList();
    }

    private UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setUserId(user.getId());
        userResponse.setImageUrl(user.getImage());
        userResponse.setIntraId(user.getIntraId());

        return userResponse;
    }
}
