package com.code2am.stocklog.domain.labels.models.dto;

import lombok.Data;

@Data
public class LabelsDTO {

    // PK
    private Integer labelsId;

    // 라벨 제목
    private String labelsTitle;

    // 라벨 상태
    private String labelsStatus;

//    // 노트 FK
//    private Integer noteId;

}
