package com.code2am.stocklog.domain.comments.service;

import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.comments.repository.CommentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommentsService {

    @Autowired
    private CommentsRepository commentsRepository;

    /**
     * 코멘트 등록
     * */
    public String createComment(Comments comments) {

        if(comments.getComment().isEmpty()){
            System.out.println("코멘트 비어있음");
            return null;
        }

        comments.setCommentDate(LocalDateTime.now());
        comments.setStatus("Y");

        commentsRepository.save(comments);
        return "코멘트 등록 성공";
    }
}
