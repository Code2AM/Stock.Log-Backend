package com.code2am.stocklog.domain.comments.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentsDTO {

    private Integer commentId;

    private String comment;

    private LocalDateTime commentDate;

    private String status;

    private Integer journalId;
}
