package ggs.srr.api.controller.studtygroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.controller.studtygroup.request.StudyGroupCreateRequest;
import ggs.srr.service.studygroup.exception.NoSuchUserException;
import ggs.srr.service.studygroup.StudyGroupService;
import ggs.srr.service.studygroup.request.StudyGroupCreateServiceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudyGroupController.class)
class StudyGroupControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StudyGroupService studyGroupService;

    @DisplayName("스터디 그룹을 생성할 수 있어야 한다.")
    @Test
    void createStudyGroup() throws Exception {

        StudyGroupCreateRequest request = new StudyGroupCreateRequest(List.of(1L, 2L, 3L), "test_group");

        mockMvc.perform(
                        post("/study_groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @DisplayName("스터디그룹을 만들 때 id 리스트를 null 로 할 수 없다.")
    @Test
    void idListNull() throws Exception {

        StudyGroupCreateRequest request = new StudyGroupCreateRequest(null, "test_group");

        mockMvc.perform(
                        post("/study_groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("사용자 id list 는 필수 입니다."));
    }

    @DisplayName("스터디그룹을 만들 때 id 리스트는 비어있을 수 없다.")
    @Test
    void emptyIdList() throws Exception {

        StudyGroupCreateRequest request = new StudyGroupCreateRequest(List.of(), "test_group");

        mockMvc.perform(
                        post("/study_groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("사용자 id가 비어있을 수 없습니다."));
    }

    @DisplayName("id 리스트 안에 존재하지 않는 회원의 id 가 들어올 수 없다.")
    @Test
    void notExistIds() throws Exception {

        StudyGroupCreateRequest request = new StudyGroupCreateRequest(List.of(1L, 2L, 3L), "test_group");

        Mockito.doThrow(new NoSuchUserException("존재하지 않는 회원입니다."))
                .when(studyGroupService).createStudyGroup(any(StudyGroupCreateServiceRequest.class));

        mockMvc.perform(
                        post("/study_groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }

    @DisplayName("id 리스트 안에 null 이 들어올 수 없다.")
    @Test
    void findIdsContainsNull() throws Exception {

        ArrayList<Long> userIds = new ArrayList<>();
        userIds.add(null);
        userIds.add(1L);

        StudyGroupCreateRequest request = new StudyGroupCreateRequest(userIds, "test_group");

        Mockito.doThrow(new NoSuchUserException("사용자 조회시 id 로 null 을 입력할 수 없습니다."))
                .when(studyGroupService).createStudyGroup(any(StudyGroupCreateServiceRequest.class));

        mockMvc.perform(
                        post("/study_groups")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("사용자 조회시 id 로 null 을 입력할 수 없습니다."));
    }


    @DisplayName("사용자의 그룹을 조회할 수 있다.")
    @Test
    void getUserStudyGroups() throws Exception {
        mockMvc.perform(
                        get("/study_groups/{userId}", 3L)
                )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success"));
    }

}