package ggs.srr.repository.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.exception.repository.common.FindByNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
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

}
