package ggs.srr.repository.user;

import ggs.srr.domain.user.FtUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public long save(FtUser user){
        em.persist(user);
        return user.getId();
    }

    public FtUser findById(long id){
        return em.find(FtUser.class, id);
    }

    public Optional<FtUser> findByIntraId(String intraId) {
        return em.createQuery("select u from FtUser u where u.intraId = :intraId" , FtUser.class)
                .setParameter("intraId", intraId)
                .getResultList().stream().findAny();
    }

    public List<FtUser> findAll() {
        return em.createQuery("select u from FtUser u", FtUser.class)
                .getResultList();
    }
}
