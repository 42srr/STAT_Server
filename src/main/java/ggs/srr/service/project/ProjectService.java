package ggs.srr.service.project;

import ggs.srr.domain.project.Project;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.project.request.CreateProjectServiceRequest;
import ggs.srr.service.project.response.ProjectInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public void save(CreateProjectServiceRequest request) {

        Project project = Project.builder()
                .name(request.getProjectName())
                .build();

        projectRepository.save(project);

    }

    public List<ProjectInformationResponse> findAll() {
        return projectRepository.findAll().stream()
                .map(Project::getName)
                .map(ProjectInformationResponse::new)
                .toList();
    }
}
