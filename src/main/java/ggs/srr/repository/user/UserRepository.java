package ggs.srr.repository.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.common.FindByNullException;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(User user){
        em.persist(user);
    }

    public Optional<User> findById(Long id){
        if (id == null) {
            throw new FindByNullException("사용자 조회시 id 로 null 을 입력할 수 없습니다.");
        }

        return Optional.ofNullable(em.find(User.class, id));
    }

}
