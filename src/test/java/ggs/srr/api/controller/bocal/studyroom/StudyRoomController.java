package ggs.srr.api.controller.bocal.studyroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.controller.bocal.StudyRoomController;
import ggs.srr.api.controller.bocal.dto.CreateStudyRoomRequest;
import ggs.srr.service.studyroom.StudyRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudyRoomController.class)
class StudyRoomControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    StudyRoomService studyRoomService;

    @DisplayName("스터디 룸을 생성할 수 있어야 한다.")
    @Test
    void createStudyRoom() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name("테스트")
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(4)
                .build();

        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Successfully created a study room"));
    }

    @DisplayName("스터디룸 만들 때 id를 null 로 할 수 없다.")
    @Test
    void idNull() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(null)
                .name("테스트")
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(12)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("스터디룸 id는 필수입니다."));
    }

    @DisplayName("스터디룸 만들 때 name을 null 로 할 수 없다.")
    @Test
    void nameNull() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name(null)
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(12)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("스터디 룸 이름은 필수입니다."));
    }

    @DisplayName("스터디룸 만들 때 img null 로 할 수 없다.")
    @Test
    void imgNull() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name("test")
                .img(null)
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(12)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("스터디 룸 사진은 필수입니다."));
    }

    @DisplayName("스터디룸 만들 때 openTime null 로 할 수 없다.")
    @Test
    void openTimeNull() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name("asd")
                .img("https://example.com/studyroom.jpg")
                .openTime(null)
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(12)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("스터디룸 오픈 시간은 필수입니다."));
    }

    @DisplayName("스터디룸 만들 때 closetime을 null 로 할 수 없다.")
    @Test
    void closeTimeNull() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name(null)
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(null)
                .option(12)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("스터디룸 닫는 시간은 필수입니다."));
    }

    @DisplayName("스터디룸 만들 때 option값을 -1로 할 수 없다.")
    @Test
    void optionMinus() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name(null)
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(-1)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("option은 최소 0부터 시작입니다."));
    }

    @DisplayName("스터디룸 만들 때 option값은 null일 수 없다.")
    @Test
    void optionZero() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name(null)
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(null)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("option은 널일 수 없습니다."));
    }

    @DisplayName("스터디룸 만들 때 option값은 16이상 불가능하다")
    @Test
    void optionFive() throws Exception {

        CreateStudyRoomRequest request = CreateStudyRoomRequest.builder()
                .studyRoomId(1L)
                .name(null)
                .img("https://example.com/studyroom.jpg")
                .openTime(LocalDateTime.now())
                .closeTime(LocalDateTime.now().plusHours(3))
                .option(16)
                .build();
        mockMvc.perform(
                        post("/studyroom")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("option은 최대 15까지 입니다."));
    }
}