package ggs.srr.service.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import ggs.srr.service.studyroom.dto.CreateStudyRoomServiceRequest;
import ggs.srr.service.studyroom.exception.InvalidRequestStudyRoomFormatException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomRepository studyRoomRepository;

    public void registerStudyroom(CreateStudyRoomServiceRequest request) {
        StudyRoom studyRoom = new StudyRoom(request.getName(), request.getImg(),request.getOpenTime(),
                                            request.getCloseTime(), request.isOpen24Hour(), request.isCanDrink(),
                                            request.isCanEat(), request.isCanUseTool());
        studyRoomRepository.save(studyRoom);
    }

    public void modifyStudyroom(CreateStudyRoomServiceRequest createStudyRoomServiceRequest) {

    }
}
