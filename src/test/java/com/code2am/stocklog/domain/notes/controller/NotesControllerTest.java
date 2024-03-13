package com.code2am.stocklog.domain.notes.controller;

import com.code2am.stocklog.domain.auth.common.controller.AuthController;
import com.code2am.stocklog.domain.notes.service.NotesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
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

    /* readNotesByUserId */
    @Test
    void readNotesByUserId() {
    }

    // 성공

    // 노트가 없는 경우

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