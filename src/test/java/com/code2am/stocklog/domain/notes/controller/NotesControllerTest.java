package com.code2am.stocklog.domain.notes.controller;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.notes.service.NotesService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private AuthUtil authUtil;

    private NotesDTO notesDTO;

    @BeforeEach
    void setup() {

        // 테스트용 notesDTO 생성
        notesDTO = new NotesDTO();
        notesDTO.setNoteId(1);
        notesDTO.setNoteName("Test Note");
        notesDTO.setNoteContents("This is a test note content.");
        notesDTO.setNoteDate(LocalDateTime.now());
        notesDTO.setNoteStatus("Active");
        notesDTO.setUserId(101);

    }

    /* readNotesByUserId */
    @Test
    void readNotesByUserId_성공() throws Exception {
        // given

        List<NotesDTO> expectedNotes = Arrays.asList(
                new NotesDTO(1, "첫 번째 노트", "This is the first note.", LocalDateTime.now()),
                new NotesDTO(2, "두 번째 노트", "This is the second note.", LocalDateTime.now())
        );

        System.out.println(expectedNotes);

        String requestBody = objectMapper.writeValueAsString(expectedNotes);

        // when
        given(notesService.readNotesByUserId()).willReturn(expectedNotes);

        // then
        mockMvc.perform(post("/notes/allNotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }




    // 노트가 없는 경우
    @Test
    void readNotesByUserId_노트가_없는_경우() throws Exception {
        // given
        List<NotesDTO> emptyList = new ArrayList<>();

        // when
        given(notesService.readNotesByUserId()).willReturn(emptyList);

        // then
        mockMvc.perform(post("/notes/allNotes"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    // 등록된 유저가 없는 경우 -> 인증되지 않은 사용자

    /* createNoteByJournalId */

    // 성공
    @Test
    void createNoteByJournalId_성공() {
        LabelsDTO fakeLabel = new LabelsDTO();
        fakeLabel.setLabelsTitle("테스트 라벨");

        NotesDTO notesDTO = NotesDTO.builder()
                .noteName("제목")
                .noteContents("내용")
                .labelsDTO(fakeLabel)
                .build();

    }



    // 입력값이 없는 경우
    @Test
    void createNoteByJournalId_실패() throws Exception {
        // given
        NotesDTO newNote = new NotesDTO(); // noteName이 null인 경우

        String error = "최소 한 글자 이상 입력해주세요"; // 예상되는 에러 메시지

        // when & then
        mockMvc.perform(post("/notes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNote)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }


    // 등록된 유저가 없는 경우 -> 인증되지 않은 사용자
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void createNote_실패_1() throws Exception {
        // given
        NotesDTO newNote = NotesDTO.builder()
                .noteName("note")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        mockMvc.perform(post("/notes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newNote)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedExceptionMessage));
    }


    /* deleteNoteByNoteId */

    // 성공
    @Test
    void deleteNoteByNoteId_성공() throws Exception {

        LabelsDTO fakeLabel = new LabelsDTO();
        fakeLabel.setLabelsTitle("테스트 라벨");

        NotesDTO notesDTO = NotesDTO.builder()
                .noteId(1)
                .noteName("제목")
                .noteContents("내용")
                .noteStatus("Y")
                .labelsDTO(fakeLabel)
                .build();

        String success = "삭제 완료";
        given(notesService.deleteNoteByNoteId(notesDTO)).willReturn(success);

        // JSON 문자열로 반환
        String requestBody = objectMapper.writeValueAsString(notesDTO);

        mockMvc.perform(post("/notes/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    // noteId가 없는 경우
    @Test
    void deleteNoteByNoteId_실패() throws Exception {
        NotesDTO notesDTO = new NotesDTO();

        String error = "노트가 없습니다.";

        mockMvc.perform(post("/notes/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notesDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));

    }

    /* updateNoteByNoteId */
    // 성공
    @Test
    void updateNoteByNoteId_성공() throws Exception {

        NotesDTO updateNote = new NotesDTO();
        String success = "수정 성공";
        given(notesService.deleteNoteByNoteId(notesDTO)).willReturn(success);

        String requestBody = objectMapper.writeValueAsString(notesDTO);

        mockMvc.perform(post("/notes/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));

    }



    // noteId가 없는 경우
    @Test
    void updateNoteByNoteId_실패() throws Exception {

        NotesDTO updateNote = new NotesDTO();

        String error = "노트가 없습니다.";

        mockMvc.perform(post("/notes/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNote)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));

    }


}