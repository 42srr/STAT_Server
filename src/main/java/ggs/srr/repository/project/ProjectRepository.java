package ggs.srr.repository.project;

import ggs.srr.domain.project.Project;
import ggs.srr.exception.repository.FindByNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Project project) {
        em.persist(project);
    }

    public Optional<Project> findById(Long id) {
        if (id == null) {
            throw new FindByNullException("project 조회시 null 로 조회할 수 없습니다.");
        }

        return Optional.ofNullable(em.find(Project.class, id));
    }

    public List<Project> findAll() {
        return em.createQuery("select p from Project p", Project.class)
                .getResultList();
    }

    public Optional<Project> findByName(String name) {
        return em.createQuery("select p from Project p where p.name = :name", Project.class)
                .setParameter("name", name)
                .getResultStream()
                .findAny();
    }
}
