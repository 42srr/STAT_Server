package ggs.srr.repository.project_user;

import ggs.srr.domain.ProjectUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectUserRepository {


    @PersistenceContext
    private EntityManager em;

    public Long save(ProjectUser projectUser){
        em.persist(projectUser);
        return projectUser.getId();
    }
}
