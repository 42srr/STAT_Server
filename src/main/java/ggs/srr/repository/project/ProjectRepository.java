package ggs.srr.repository.project;

import ggs.srr.domain.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(Project project){
        em.persist(project);
        return project.getId();
    }

    public List<Project> findAll(){
        return em.createQuery("select p from Project as p order by p.resourceId", Project.class)
                .getResultList();
    }

    public Project findByResourceId(Long resourceId){
        Project project = null;
        try {
            project = (Project) em.createQuery("select p from Project p where p.resourceId = :resourceId")
                    .setParameter("resourceId", resourceId)
                    .getSingleResult();
        }catch (NoResultException e){

        }
        return project;
    }
}
