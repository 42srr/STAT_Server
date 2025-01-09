package ggs.srr.repository.reservation.studygroup;

import ggs.srr.domain.reservation.studygroup.StudyGroup;
import ggs.srr.repository.reservation.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudyGroupRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(StudyGroup studyGroup) {
        em.persist(studyGroup);
    }

    public StudyGroup findById(Long groupId) {
        if (groupId == null) {
            throw new FindIdNullException("id 로 그룹 조회시 null 을 전달할 수 없습니다.");
        }

        return em.find(StudyGroup.class, groupId);
    }

}
