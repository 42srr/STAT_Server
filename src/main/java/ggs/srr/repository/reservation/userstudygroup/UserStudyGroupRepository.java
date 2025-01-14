package ggs.srr.repository.reservation.userstudygroup;

import ggs.srr.domain.userstudygroup.UserStudyGroup;
import ggs.srr.repository.reservation.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserStudyGroupRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(UserStudyGroup userStudyGroup) {
        em.persist(userStudyGroup);
    }

    public UserStudyGroup findById(Long userStudyGroupId) {
        if (userStudyGroupId == null) {
            throw new FindIdNullException("사용자 스터디 그룹을 id로 조회할 경우 null 을 입력할 수 없습니다.");
        }

        return em.find(UserStudyGroup.class, userStudyGroupId);
    }

    public List<UserStudyGroup> findByUserId(Long userId) {

        if (userId == null) {
            throw new FindIdNullException("사용자 스터디 그룹을 user id 로 조회할 경우 null 을 입력할 수 없습니다.");
        }

        return em.createQuery("select ug from UserStudyGroup ug where ug.user.id = :userId", UserStudyGroup.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
