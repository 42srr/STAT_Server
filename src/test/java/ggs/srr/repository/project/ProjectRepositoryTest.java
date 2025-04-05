package ggs.srr.repository.project;

import ggs.srr.domain.project.Project;
import ggs.srr.exception.repository.common.FindByNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @DisplayName("프로젝트를 저장 및 조회할 수 있다.")
    @Test
    void saveAndFind() {

        //given
        Project project = Project.builder()
                .name("test project")
                .build();

        projectRepository.save(project);

        //when
        Project findProject = projectRepository.findById(project.getId()).get();

        //then
        assertThat(findProject).isNotNull();
        assertThat(findProject.getName()).isEqualTo(findProject.getName());
    }

    @DisplayName("아이디로 조회시 null을 대입할 수 없다.")
    @Test
    void findByNull() {
        //when //then
        assertThatThrownBy(() -> projectRepository.findById(null))
                .isInstanceOf(FindByNullException.class)
                .hasMessage("project 조회시 null 로 조회할 수 없습니다.");
    }

}