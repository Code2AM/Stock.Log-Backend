package com.code2am.stocklog.domain.comments.controller;

import com.code2am.stocklog.domain.comments.models.dto.CommentsRequestDTO;
import com.code2am.stocklog.domain.comments.service.CommentsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentsController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentsControllerTest {

    @MockBean
    private MockMvc mockMvc;

    @MockBean
    private CommentsService commentsService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void createCommentByJournalId() {
    }

    @Test
    void readCommentsByJournalId() {
    }

    @Test
    void deleteCommentByCommentId() {
    }
}