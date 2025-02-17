package ggs.srr.repository.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studygroup.exception.FindIdNullException;
import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class StudyRoomRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void save(StudyRoom studyRoom) { em.persist(studyRoom); }

    public CreateStudyRoomServiceRequest findById(Long id) {
        if (id == null) {
            throw new FindIdNullException("id로 studyroom 조회시 null을 전달할 수 없습니다.");
        }
        return em.find(CreateStudyRoomServiceRequest.class, id);
    }
}
