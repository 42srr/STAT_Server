package ggs.srr.repository.user;

import ggs.srr.domain.FtUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class FtUserRepository {

    @PersistenceContext
    private EntityManager em;

    public Long save(FtUser user){
        em.persist(user);
        return user.getId();
    }

    public FtUser findById(Long id){
        return em.find(FtUser.class, id);
    }

    public FtUser findByIntraId(String intraId) {
        FtUser result = null;
        try {
            result =  (FtUser) em.createQuery("select m from FtUser m where m.intraId = :intraId")
                    .setParameter("intraId", intraId)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        return result;
    }

    public FtUser findByResourceOwnerId(Long resourceOwnerId) {
        FtUser result = null;
        try {
            result =  (FtUser) em.createQuery("select m from FtUser m where m.resourceOwnerId = :resourceOwnerId")
                    .setParameter("resourceOwnerId", resourceOwnerId)
                    .getSingleResult();
        } catch (NoResultException e) {
        }
        return result;
    }

}
