package ggs.srr.repository.project;

import ggs.srr.domain.project.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    @PersistenceContext
    private EntityManager em;

    public long save(Project project) {
        em.persist(project);
        return project.getId();
    }

    public Project findById(long id) {
        return em.find(Project.class, id);
    }

    public Optional<Project> findByProjectName(String projectName) {
        return em.createQuery("select p from Project p where p.name = :projectName", Project.class)
                .setParameter("projectName", projectName)
                .getResultList().stream().findAny();
    }

    public List<Project> findAll() {
        return em.createQuery("select p from Project p", Project.class)
                .getResultList();
    }
}
