package ggs.srr.service.studyroom;

import ggs.srr.domain.studyroom.StudyRoom;
import ggs.srr.domain.userstudygroup.UserStudyGroup;
import ggs.srr.repository.studyroom.StudyRoomRepository;
import ggs.srr.service.studygroup.request.UserAllStudyGroupServiceRequest;
import ggs.srr.service.studygroup.response.UserAllStudyGroupResponse;
import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyRoomService {
    private final StudyRoomRepository studyRoomRepository;

    public void registerStudyroom(CreateStudyRoomServiceRequest request) {
        StudyRoom studyRoom = new StudyRoom(request.getName(), request.getImg(),request.getOpenTime(),
                                            request.getCloseTime(), request.getIsOpen24Hour(), request.getCanDrink(),
                                            request.getCanEat(), request.getCanUseTool());
        studyRoomRepository.save(studyRoom);
    }


    public void modifyStudyroom(CreateStudyRoomServiceRequest createStudyRoomServiceRequest) {

    }
}
