package com.code2am.stocklog.domain.comments.models.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsVO {

    private Integer commentId;

    private String comment;

    private LocalDateTime commentDate;
}
