package com.code2am.stocklog.domain.comments.models.entity;

import com.code2am.stocklog.domain.comments.models.dto.CommentsDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_COMMENTS")
@Data
public class Comments {

    @Id
    @Column(name = "COMMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "COMMENT_DATE")
    private LocalDateTime commentDate;

    @Column(name = "STATUS")
    private String status;

    @JoinColumn(name = "JOURNAL_ID")
    @ManyToOne
    private Journals journals;

    /* DTO Converter */
    public CommentsDTO convertToDTO() {
        CommentsDTO comment = new CommentsDTO();
        comment.setCommentId(this.commentId);
        comment.setComment(this.comment);
        comment.setCommentDate(this.commentDate);
        comment.setStatus(this.status);

        return comment;
    }
}
