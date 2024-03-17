package com.code2am.stocklog.domain.comments.controller;

import com.code2am.stocklog.domain.comments.models.dto.CommentsRequestDTO;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import com.code2am.stocklog.domain.comments.repository.CommentsRepository;
import com.code2am.stocklog.domain.comments.service.CommentsService;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentsController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentsService commentsService;

    @MockBean
    private CommentsRepository commentsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 코멘트_등록_성공() throws Exception {

        Journals newJournals = new Journals();
        newJournals.setJournalId(100);

        Comments newComment = new Comments();
        newComment.setComment("test");
        newComment.setJournals(newJournals);

        String success = "코멘트 등록 성공";

        given(commentsService.createComment(newComment)).willReturn(success);

        String requestBody = objectMapper.writeValueAsString(newComment);

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 코멘트_없음() throws Exception {

        Journals newJournals = new Journals();
        newJournals.setJournalId(100);

        Comments comments = new Comments();
        comments.setJournals(newJournals);

        String error = "코멘트를 입력해주세요.";

        given(commentsService.createComment(comments)).willReturn(error);

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 조회된_코멘트가_없음() throws Exception {

        CommentsRequestDTO comments = new CommentsRequestDTO();
        comments.setJournalId(100);

        given(commentsService.readCommentsByJournalId(comments.getJournalId())).willReturn(null);

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments/read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void 코멘트_조회_성공() throws Exception {

        CommentsRequestDTO comments = new CommentsRequestDTO();
        comments.setJournalId(100);

        CommentsVO test1 = new CommentsVO();
        test1.setCommentId(100);
        test1.setComment("test1");
        test1.setCommentDate(LocalDateTime.now());

        CommentsVO test2 = new CommentsVO();
        test2.setCommentId(100);
        test2.setComment("test1");
        test2.setCommentDate(LocalDateTime.now());

        List<CommentsVO> test = new ArrayList<CommentsVO>();
        test.add(test1);
        test.add(test2);

        given(commentsService.readCommentsByJournalId(comments.getJournalId())).willReturn(test);

        String requestBody = objectMapper.writeValueAsString(comments);

        String expectedJson = objectMapper.writeValueAsString(test);

        mockMvc.perform(post("/comments/read")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void 코멘트_삭제_성공() throws Exception {

        CommentsRequestDTO comments = new CommentsRequestDTO();
        comments.setJournalId(100);

        String success = "코멘트 삭제 성공";

        given(commentsService.deleteCommentByCommentId(comments.getCommentId())).willReturn(success);

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 삭제할_코멘트_정보가_없는_경우() throws Exception {

        CommentsRequestDTO comments = new CommentsRequestDTO();

        String error = "입력된 정보가 없습니다.";

        given(commentsService.deleteCommentByCommentId(comments.getCommentId())).willReturn(error);

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(error));
    }

    @Test
    void 해당되는_코멘트가_존재하지_않을_경우() throws Exception {
        CommentsRequestDTO comments = new CommentsRequestDTO();
        comments.setCommentId(100); // commentId를 설정

        String error = "해당하는 데이터가 없습니다.";

        given(commentsRepository.findById(comments.getCommentId())).willReturn(Optional.empty());
        given(commentsService.deleteCommentByCommentId(comments.getCommentId())).willReturn("404");

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string(error));
    }

    @Test
    void 이미_삭제된_코멘트를_삭제하는_경우() throws Exception {
        CommentsRequestDTO comments = new CommentsRequestDTO();
        comments.setCommentId(100);

        Comments deletedComment = new Comments();
        deletedComment.setCommentId(100);
        deletedComment.setStatus("N");

        String error = "이미 변경된 요청입니다.";

        given(commentsRepository.findById(comments.getCommentId())).willReturn(Optional.of(deletedComment));
        given(commentsService.deleteCommentByCommentId(comments.getCommentId())).willReturn("400");

        String requestBody = objectMapper.writeValueAsString(comments);

        mockMvc.perform(post("/comments/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }
}