package ggs.srr.repository.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.exception.repository.FindByNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProjectUserRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(ProjectUser projectUser) {
        em.persist(projectUser);
    }

    public Optional<ProjectUser> findById(Long id) {
        if (id == null) {
            throw new FindByNullException("사용자의 프로젝트를 조회시 null 을 대입할 수 없습니다.");
        }

        return Optional.ofNullable(em.find(ProjectUser.class, id));
    }

    public List<ProjectUser> findAll() {
        return em.createQuery("select pu from ProjectUser pu", ProjectUser.class)
                .getResultList();
    }

    public List<ProjectUser> findByUserIdAndStatus(Long userId, ProjectUserStatus status) {
        return em.createQuery("select pu from ProjectUser pu where pu.user.id = :userId and pu.status = :status", ProjectUser.class)
                .setParameter("userId", userId)
                .setParameter("status", status)
                .getResultList();
    }

    public List<ProjectUser> findByUserId(Long userId) {
        return em.createQuery("select pu from ProjectUser pu where pu.user.id = :userId", ProjectUser.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Optional<ProjectUser> findByUserIdAdnProjectId(Long userId, Long projectId) {
        return em.createQuery("select pu from ProjectUser pu where pu.user.id = :userId and pu.project.id = :projectId", ProjectUser.class)
                .setParameter("userId", userId)
                .setParameter("projectId", projectId)
                .getResultStream().findAny();
    }

    public void deleteAll() {
        findAll().stream()
                .forEach(projectUser -> em.remove(projectUser));
    }
}
