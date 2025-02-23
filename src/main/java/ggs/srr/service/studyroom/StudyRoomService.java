package ggs.srr.service.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;

    public void registerStudyRoom(CreateStudyRoomServiceRequest request) {

        // builder pattern 고려
        StudyRoom studyRoom = new StudyRoom(request.getName(), request.getImg(),request.getOpenTime(),
                                            request.getCloseTime(), request.getOpen24Flag(), request.getDrinkFlag(),
                                            request.getEatFlag(), request.getUseToolFlag());
        studyRoomRepository.save(studyRoom);
    }

    /*
        todo
        1. study room 수정
        2. study room 삭제
    */
}
