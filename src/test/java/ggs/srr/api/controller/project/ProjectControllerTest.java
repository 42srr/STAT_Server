package ggs.srr.api.controller.project;

import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.service.projectuser.ProjectUserService;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProjectUserService projectUserService;

    @DisplayName("사용자의 프로젝트 분포를 확인할 수 있다.")
    @Test
    void getProjectsDistribution() throws Exception {
        //given

        when(projectUserService.getProjectUserDistribution())
                .thenReturn(createProjectDistribution());

        //when //then
        mockMvc.perform(
                        get("/api/projects/distribution")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("사용자의 종료 프로젝트 분포를 확인할 수 있다.")
    @Test
    void getProjectsDistributionFinished() throws Exception {
        //given

        when(projectUserService.getProjectUserDistributionFinished())
                .thenReturn(createProjectDistribution());

        //when //then
        mockMvc.perform(
                        get("/api/projects/distribution?type=finished")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    @DisplayName("사용자의 진행 프로젝트 분포를 확인할 수 있다.")
    @Test
    void getProjectsDistributionInprogress() throws Exception {
        //given

        when(projectUserService.getProjectUserDistributionInProgress())
                .thenReturn(createProjectDistribution());

        //when //then
        mockMvc.perform(
                        get("/api/projects/distribution?type=in-progress")
                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").exists());
    }

    private ProjectUserDistributionResponse createProjectDistribution() {
        Map<String, Map<ProjectUserStatus, Long>> distribution = new HashMap<>();
        return new ProjectUserDistributionResponse(distribution);
    }

}