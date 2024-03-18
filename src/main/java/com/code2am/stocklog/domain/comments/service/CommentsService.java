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
import java.util.Objects;
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
    public String createComment(Comments comments) {

        if(comments.getComment().isEmpty()){
            return "코멘트가 없습니다.";
        }

        comments.setCommentDate(LocalDateTime.now());
        comments.setStatus("Y");

        commentsRepository.save(comments);
        return "코멘트 등록 성공";
    }

    /**
     * 코멘트 조회
     * */
    public List<CommentsVO> readCommentsByJournalId(Integer journalId) {

        if(Objects.isNull(journalId)){
            return null;
        }

        return commentsDAO.readCommentsByJournalId(journalId);
    }

    /**
     * 코멘트 삭제
     * */
    public String deleteCommentByCommentId(Integer commentId) {

        Optional<Comments> deleteComment = commentsRepository.findById(commentId);

        if (deleteComment.isEmpty()) {
            return "404"; // 존재하지 않는 코멘트를 삭제할 경우 실패로 간주
        }

        Comments delete = deleteComment.get();

        if ("N".equals(delete.getStatus())) {
            return "400"; // 이미 삭제된 코멘트를 삭제할 경우 실패로 간주
        }

        delete.setStatus("N");
        delete.setCommentDate(LocalDateTime.now());
        commentsRepository.save(delete);

        return "코멘트 삭제 성공";
    }
}
