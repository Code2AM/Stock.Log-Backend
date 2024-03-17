package com.code2am.stocklog.domain.comments.models.dto;

import com.code2am.stocklog.domain.comments.models.entity.Comments;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsRequestDTO {

    private Integer commentId;

    @NotBlank(message = "코멘트를 입력해주세요.")
    private String comment;

    private LocalDateTime commentDate;

    private String status;

    private Integer journalId;

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
