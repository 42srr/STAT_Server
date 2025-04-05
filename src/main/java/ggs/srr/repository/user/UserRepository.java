package ggs.srr.repository.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.repository.common.FindByNullException;
import ggs.srr.repository.user.dto.UserRankQueryDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<User> getRankByLevel(UserRankQueryDto dto) {
        return em.createQuery("select u from User u order by u.level desc", User.class)
                .setFirstResult(dto.getStartPosition())
                .setMaxResults(dto.getMaxResult())
                .getResultList();
    }

    public List<User> getRankByWallet(UserRankQueryDto dto) {
        return em.createQuery("select u from User u order by u.wallet desc", User.class)
                .setFirstResult(dto.getStartPosition())
                .setMaxResults(dto.getMaxResult())
                .getResultList();
    }

    public List<User> getRankByCollectionPoint(UserRankQueryDto dto) {
        return em.createQuery("select u from User u order by u.collectionPoint desc", User.class)
                .setFirstResult(dto.getStartPosition())
                .setMaxResults(dto.getMaxResult())
                .getResultList();
    }


}
