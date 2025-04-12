package ggs.srr.service.project;

import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.project.request.CreateProjectServiceRequest;
import ggs.srr.service.project.response.ProjectInformationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    @DisplayName("모든 프로젝트를 조회할 수 있다.")
    @Test
    void findAll() {
        //given
        CreateProjectServiceRequest request1 = new CreateProjectServiceRequest("test1");
        CreateProjectServiceRequest request2 = new CreateProjectServiceRequest("test2");
        CreateProjectServiceRequest request3 = new CreateProjectServiceRequest("test3");
        CreateProjectServiceRequest request4 = new CreateProjectServiceRequest("test4");

        projectService.save(request1);
        projectService.save(request2);
        projectService.save(request3);
        projectService.save(request4);

        //when
        List<ProjectInformationResponse> projectInformation = projectService.findAll();

        //then
        assertThat(projectInformation).hasSize(4);
    }

}