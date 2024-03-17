package com.code2am.stocklog.domain.comments.service;

import com.code2am.stocklog.domain.comments.dao.CommentsDAO;
import com.code2am.stocklog.domain.comments.models.dto.CommentsRequestDTO;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import com.code2am.stocklog.domain.comments.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    @Autowired
    private CommentsDAO commentsDAO;

    /**
     * 코멘트 등록
     * */
    public String createComment(CommentsRequestDTO comments) {

        if(comments.getComment().isEmpty()){
            System.out.println("코멘트 비어있음");
            return null;
        }


        Comments add = new Comments();

        add.getJournals().setJournalId(comments.getJournalId());
        add.setComment(comments.getComment());
        add.setCommentDate(LocalDateTime.now());
        add.setStatus("Y");

        commentsRepository.save(add);
        return "코멘트 등록 성공";
    }

    /**
     * 코멘트 조회
     * */
    public List<CommentsVO> readCommentsByJournalId(Integer journalId) {

        return commentsDAO.readCommentsByJournalId(journalId);
    }

    /**
     * 코멘트 삭제
     * */
    public String deleteCommentByCommentId(Integer commentId) {

        Optional<Comments> deleteComment = commentsRepository.findById(commentId);

        if(deleteComment.isEmpty()){
            return "존재하지 않는 데이터입니다.";
        }else if(deleteComment.get().getStatus().equals("N")){
            return "이미 삭제된 데이터입니다.";
        }

        Comments delete = deleteComment.get();
        delete.setStatus("N");
        delete.setCommentDate(LocalDateTime.now());
        commentsRepository.save(delete);

        return "코멘트 삭제 성공";
    }
}
