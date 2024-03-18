package com.code2am.stocklog.domain.comments.models.dto;

import com.code2am.stocklog.domain.comments.models.entity.Comments;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class CommentsRequestDTO {

    private Integer commentId;

    private String comment;

    private LocalDateTime commentDate;

    private String status;

    private Integer journalId;

    @Builder
    public CommentsRequestDTO(Integer commentId, String comment, LocalDateTime commentDate, String status, Integer journalId) {
        this.commentId = commentId;
        this.comment = comment;
        this.commentDate = commentDate;
        this.status = status;
        this.journalId = journalId;
    }

    /* Entity Converter */
    public Comments convertToEntity() {
        Comments comment = new Comments();
       comment.setCommentId(this.commentId);
       comment.setComment(this.comment);
       comment.setCommentDate(this.commentDate);
       comment.setStatus(this.status);

        return comment;
    }
}
