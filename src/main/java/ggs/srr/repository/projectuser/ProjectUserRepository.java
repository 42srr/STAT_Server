package ggs.srr.repository.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectUserRepository {

    @PersistenceContext
    private EntityManager em;

    public long save(ProjectUser projectUser) {
        em.persist(projectUser);
        return projectUser.getId();
    }
}
