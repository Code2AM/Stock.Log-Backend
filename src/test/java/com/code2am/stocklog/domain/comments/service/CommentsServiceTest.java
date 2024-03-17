package com.code2am.stocklog.domain.comments.service;

import com.code2am.stocklog.domain.comments.dao.CommentsDAO;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import com.code2am.stocklog.domain.comments.repository.CommentsRepository;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentsServiceTest {

    @InjectMocks
    private CommentsService commentsService;

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private CommentsDAO commentsDAO;

    @Test
    void 코멘트_등록_성공() {

        Journals journals = new Journals();
        journals.setJournalId(100);

        Comments comments = new Comments();
        comments.setComment("test");
        comments.setJournals(journals);

        Comments data = new Comments();
        data.setCommentId(1);
        data.setComment("test");
        data.setJournals(journals);
        data.setStatus("Y");
        data.setCommentDate(LocalDateTime.now());
        given(commentsRepository.save(comments)).willReturn(data);

        String result = commentsService.createComment(comments);

        assertEquals("코멘트 등록 성공", result);
    }

    @Test
    void 코멘트가_없는_경우(){

        Journals journals = new Journals();
        journals.setJournalId(100);

        Comments comments = new Comments();
        comments.setComment("");

        String expected = "코멘트가 없습니다.";

        String result = commentsService.createComment(comments);

        assertEquals(expected, result);
    }

    @Test
    void 코멘트_조회_성공() {
        Integer jouralId = 100;

        CommentsVO comment1 = new CommentsVO();
        comment1.setCommentId(1);
        comment1.setComment("test1");
        comment1.setCommentDate(LocalDateTime.now());

        CommentsVO comment2 = new CommentsVO();
        comment2.setCommentId(2);
        comment2.setComment("test2");
        comment2.setCommentDate(LocalDateTime.now());

        List<CommentsVO> list = new ArrayList<CommentsVO>();
        list.add(comment1);
        list.add(comment2);

        given(commentsDAO.readCommentsByJournalId(jouralId)).willReturn(list);

        List<CommentsVO> result = commentsService.readCommentsByJournalId(jouralId);

        Assertions.assertEquals(list, result);

    }

    @Test
    void 조회에_성공하였으나_코멘트가_존재하지_않을_경우(){
        Integer jouralId = 100;
        List<CommentsVO> list = new ArrayList<CommentsVO>();

        given(commentsDAO.readCommentsByJournalId(jouralId)).willReturn(list);

        List<CommentsVO> result = commentsService.readCommentsByJournalId(jouralId);

        Assertions.assertEquals(list, result);
    }

    @Test
    void 매매일지_정보가_넘어오지_않은_경우(){

        Integer journalId = null;

        List<CommentsVO> result = commentsService.readCommentsByJournalId(journalId);

        assertNull(result);
    }
    @Test
    void 코멘트_삭제_성공() {
        Journals journals = new Journals();
        journals.setJournalId(100);

        Comments comments = new Comments();
        comments.setCommentId(100);
        comments.setComment("text");
        comments.setStatus("Y");
        comments.setCommentDate(LocalDateTime.now());
        comments.setJournals(journals);

        given(commentsRepository.findById(comments.getCommentId())).willReturn(Optional.of(comments));

        String result = commentsService.deleteCommentByCommentId(comments.getCommentId());
        assertEquals("코멘트 삭제 성공", result);
    }

    @Test
    void 코멘트가_존재하지_않는_경우(){
        Integer commentId = 100;

        given(commentsRepository.findById(commentId)).willReturn(Optional.empty());

        String result = commentsService.deleteCommentByCommentId(commentId);

        assertEquals("404", result);
    }

    @Test
    void 이미_삭제된_코멘트를_삭제할_경우(){
        Integer commentId = 100;

        Journals journals = new Journals();
        journals.setJournalId(100);

        Comments comments = new Comments();
        comments.setCommentId(100);
        comments.setComment("text");
        comments.setStatus("N");
        comments.setCommentDate(LocalDateTime.now());
        comments.setJournals(journals);

        given(commentsRepository.findById(commentId)).willReturn(Optional.ofNullable(comments));

        String result = commentsService.deleteCommentByCommentId(commentId);

        assertEquals("400", result);
    }
}