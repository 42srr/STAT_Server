//package ggs.srr.service.reservation.studyroom;
//
//import ggs.srr.repository.studyroom.StudyRoomRepository;
//import ggs.srr.service.studyroom.StudyRoomService;
//import ggs.srr.service.studyroom.request.CreateStudyRoomServiceRequest;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test")
//class StudyRoomServiceTest {
//
//    @Autowired
//    StudyRoomRepository studyRoomRepository;
//
//    @Autowired
//    StudyRoomService studyRoomService;
//
//
//    @DisplayName("스터디룸을 생성할 수 있다.")
//    @Test
//    void createStudyRoom() {
//        CreateStudyRoomServiceRequest request1 = CreateStudyRoomServiceRequest.builder()
//                .studyRoomId(1L)
//                .name("테스트")
//                .img("https://example.com/studyroom.jpg")
//                .openTime(LocalDateTime.now())
//                .closeTime(LocalDateTime.now().plusHours(3))
//                .isOpen24Hour(1)
//                .canDrink(1)
//                .canEat(0)
//                .canUseTool(1)
//                .build();
//
//        CreateStudyRoomServiceRequest request2 = CreateStudyRoomServiceRequest.builder()
//                .studyRoomId(1L)
//                .name("테스트2")
//                .img("https://example.com/studyroom.jpg")
//                .openTime(LocalDateTime.now())
//                .closeTime(LocalDateTime.now().plusHours(3))
//                .isOpen24Hour(0)
//                .canDrink(0)
//                .canEat(0)
//                .canUseTool(0)
//                .build();
//        studyRoomService.registerStudyroom(request1);
//        studyRoomService.registerStudyroom(request2);
//    }
//}