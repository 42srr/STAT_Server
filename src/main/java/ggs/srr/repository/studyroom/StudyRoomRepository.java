package ggs.srr.repository.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class StudyRoomRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(StudyRoom studyRoom) {
        em.persist(studyRoom);
    }

    public StudyRoom findById(Long id) {
        if (id == null) {
            throw new FindIdNullException("id로 studyroom 조회시 null을 전달할 수 없습니다.");
        }

        return em.find(StudyRoom.class, id);
    }

    public StudyRoom findByIdForUpdate(Long id) {
        if (id == null) {
            throw new FindIdNullException("id로 studyroom 조회시 null을 전달할 수 없습니다.");
        }
        return em.find(StudyRoom.class, id, LockModeType.PESSIMISTIC_READ);
    }

}
