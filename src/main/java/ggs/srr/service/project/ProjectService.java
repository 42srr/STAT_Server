package ggs.srr.service.project;

import ggs.srr.domain.project.Project;
import ggs.srr.repository.projectuser.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = false)
    public long save(Project project) {
        return projectRepository.save(project);
    }

    public Optional<Project> findByProjectName(String projectName) {
        return projectRepository.findByProjectName(projectName);
    }

    public Project findById(long id) {
        return projectRepository.findById(id);
    }
}
