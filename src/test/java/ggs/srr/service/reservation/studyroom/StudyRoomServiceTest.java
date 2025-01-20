package ggs.srr.service.reservation.studygroup;

import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.studygroup.StudyGroupService;
import ggs.srr.service.studygroup.exception.NoSuchUserException;
import ggs.srr.service.studygroup.request.StudyGroupCreateServiceRequest;
import ggs.srr.service.studygroup.request.UserAllStudyGroupServiceRequest;
import ggs.srr.service.studygroup.response.UserAllStudyGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class StudyGroupServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudyGroupService studyGroupService;


    @DisplayName("스터디 그룹을 생성 및 조회할 수 있다.")
    @Test
    void createStudyGroup() {

        List<Long> userIds = createUsers();
        StudyGroupCreateServiceRequest request1 = new StudyGroupCreateServiceRequest(userIds, "test_group1");
        StudyGroupCreateServiceRequest request2 = new StudyGroupCreateServiceRequest(userIds, "test_group2");

        studyGroupService.createStudyGroup(request1);
        studyGroupService.createStudyGroup(request2);

        List<UserAllStudyGroupResponse> response = studyGroupService.getUsersStudyGroupResponse(new UserAllStudyGroupServiceRequest(userIds.get(0)));

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getGroupName()).isEqualTo("test_group1");
        assertThat(response.get(1).getGroupName()).isEqualTo("test_group2");
        assertThat(response.get(0).getSize()).isEqualTo(3);
        assertThat(response.get(1).getSize()).isEqualTo(3);

    }

    @DisplayName("존재하지 않는 회원은 스터디 그룹을 생성할 수 없다.")
    @Test
    void notExistUserCreateStudyGroup() {
        assertThatThrownBy(() -> studyGroupService.createStudyGroup(new StudyGroupCreateServiceRequest(List.of(10L), "test")))
                .isInstanceOf(NoSuchUserException.class);
    }


    private List<Long> createUsers() {
        FtUser user1 = new FtUser();
        FtUser user2 = new FtUser();
        FtUser user3 = new FtUser();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        return List.of(user1.getId(), user2.getId(), user3.getId());
    }
}