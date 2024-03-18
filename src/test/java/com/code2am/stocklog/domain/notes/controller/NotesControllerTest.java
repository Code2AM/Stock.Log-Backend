package com.code2am.stocklog.domain.notes.controller;

import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(NotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotesControllerTest {

    @MockBean
    private MockMvc mockMvc;

    @MockBean
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

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
        mockMvc.perform(post("/notes/allNotes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(requestBody));
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("[]"));
    }

    // 등록된 유저가 없는 경우 -> 인증되지 않은 사용자

    /* createNoteByJournalId */
    @Test
    void createNoteByJournalId() {
    }

    // 성공

    // 입력값이 없는 경우

    // 등록된 유저가 없는 경우 -> 인증되지 않은 사용자


    /* deleteNoteByNoteId */
    @Test
    void deleteNoteByNoteId() {
    }

    // 성공

    // noteId가 없는 경우


    /* updateNoteByNoteId */
    @Test
    void updateNoteByNoteId() {
    }

    // 성공

    // noteId가 없는 경우


}