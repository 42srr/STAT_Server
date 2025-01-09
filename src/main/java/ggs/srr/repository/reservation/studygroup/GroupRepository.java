package ggs.srr.repository.reservation.studygroup;

import ggs.srr.domain.reservation.studygroup.StudyGroup;
import ggs.srr.repository.reservation.exception.group.NullGroupIdException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class GroupRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(StudyGroup studyGroup) {
        em.persist(studyGroup);
    }

    public StudyGroup findById(Long groupId) {
        if (groupId == null) {
            throw new NullGroupIdException("id 로 그룹 조회시 null 을 전달할 수 없습니다.");
        }

        return em.find(StudyGroup.class, groupId);
    }

}
