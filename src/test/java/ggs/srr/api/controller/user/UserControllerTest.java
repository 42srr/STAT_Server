package ggs.srr.api.controller.user;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.Role;
import ggs.srr.domain.user.User;
import ggs.srr.exception.user.UserErrorCode;
import ggs.srr.exception.user.UserException;
import ggs.srr.service.client.APIClient;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.projectuser.request.ProjectUserRequest;
import ggs.srr.service.projectuser.request.ProjectUsersRequest;
import ggs.srr.service.projectuser.response.ProjectUserInformationResponse;
import ggs.srr.service.user.UserService;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import ggs.srr.service.user.response.UserUpdateResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    ProjectUserService projectUserService;

    @MockBean
    APIClient apiClient;

    @DisplayName("단일 사용자의 정보를 조회할 수 있다.")
    @Test
    void findById() throws Exception {
        //given
        when(userService.findById(any(UserInformationServiceRequest.class), any(LocalDateTime.class)))
                .thenReturn(new UserInformationResponse(createUser("test"), true));

        //when //then
        mockMvc.perform(
                get("/api/users/10")
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("userId 반드시 숫자여야 한다.")
    @Test
    void notNumericUserId() throws Exception {

        //given
        when(userService.findById(any(UserInformationServiceRequest.class), any(LocalDateTime.class)))
                .thenReturn(new UserInformationResponse(createUser("test"), true));

        //when //then
        mockMvc.perform(
                get("/api/users/qwe")
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").value("올바르지 않은 타입입니다. 다시 확인해 주세요"));
    }

    @DisplayName("찾는 사용자가 없을 경우 400 응답을 반환한다.")
    @Test
    void notExistsUser() throws Exception {
        //given
        when(userService.findById(any(UserInformationServiceRequest.class), any(LocalDateTime.class)))
                .thenThrow(new UserException(UserErrorCode.NOT_FOUND_USER));

        //when //then
        mockMvc.perform(
                get("/api/users/10")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").value("해당 사용자를 조회할 수 없습니다."));
    }

    @DisplayName("사용자 레벨 분포를 확인할 수 있다.")
    @Test
    void getLevelDistribution() throws Exception {
        //given
        when(userService.getUserLevelDistribution())
                .thenReturn(createLevelDistribution());

        //when //then
        mockMvc.perform(
                get("/api/users/distribution/level")
        )
                .andDo(print()).
                andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.distribution").isMap());
    }

    @DisplayName("랭킹을 조회할 수 있다.")
    @Test
    void getRanking() throws Exception {
        //given
        when(userService.getUserRankingOfLevel(any(UserRankingServiceRequest.class)))
                .thenReturn(List.of());

        //when //then
        mockMvc.perform(
                get("/api/users/ranking")
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("사용자의 프로젝트를 조회할 수 있다.")
    @Test
    void getUserProjects() throws Exception {
        //given
        when(projectUserService.getUserProjects(any(ProjectUsersRequest.class)))
                .thenReturn(List.of());

        //when //then
        mockMvc.perform(
                get("/api/users/10/projects")
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @DisplayName("시용자 개인의 프로젝트를 조회할 수 있다.")
    @Test
    void getUserProject() throws Exception {
        //given
        when(projectUserService.getUserProject(any(ProjectUserRequest.class)))
                .thenReturn(createProjectUserResponse());

        //when //then
        mockMvc.perform(
                get("/api/users/1/projects/10")
        )
                .andDo(print())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("사용자 개인의 정보를 스스로 업데이트 할 수 있다.")
    @Test
    void updateUserSelf() throws Exception {
        when(userService.findById(any(UserInformationServiceRequest.class), any(LocalDateTime.class)))
                .thenReturn(new UserInformationResponse(createUser("testuser"), true));

        UserUpdateResponse anyResponse = new UserUpdateResponse(
                createUser("testuser"),
                false,
                List.of(),
                List.of()
        );

        when(userService.updateUserProfile(any(Long.class), any()))
                .thenReturn(anyResponse);

        mockMvc.perform(
                        patch("/api/users/10")
                                .header("Authorization", "Bearer mock-jwt-token")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").exists());

    }

    // UserId가 없을 때

    private User createUser(String intraId) {
        return User.builder()
                .intraId(intraId)
                .role(Role.CADET)
                .wallet(10)
                .correctionPoint(10)
                .image("image url")
                .build();
    }

    private LevelDistributionResponse createLevelDistribution() {
        Map<Integer, Long> distribution = new HashMap<>();
        distribution.put(1, 3L);
        distribution.put(2, 5L);
        distribution.put(3, 1L);

        return  new LevelDistributionResponse(distribution);
    }

    private ProjectUserInformationResponse createProjectUserResponse() {
        User user = createUser("test");
        Project project = Project.builder()
                .name("test")
                .build();

        ProjectUser projectUser = ProjectUser.builder()
                .finalMark(100)
                .user(user)
                .project(project)
                .build();

        return new ProjectUserInformationResponse(projectUser);
    }
}
