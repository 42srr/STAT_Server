package ggs.srr.service.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.repository.ProjectUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserService(ProjectUserRepository projectUserRepository) {
        this.projectUserRepository = projectUserRepository;
    }

    @Transactional(readOnly = false)
    public long save(ProjectUser projectUser) {
        return projectUserRepository.save(projectUser);
    }
}
