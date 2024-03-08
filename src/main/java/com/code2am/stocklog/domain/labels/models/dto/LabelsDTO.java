package com.code2am.stocklog.domain.labels.models.dto;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import lombok.Data;

@Data
public class LabelsDTO {

    // PK
    private Integer labelsId;

    // 라벨 제목
    private String labelsTitle;

    // 라벨 상태
    private String labelsStatus;

    private Integer userId;

//    // 노트 FK
//    private Integer noteId;


    /* Entity Converter */
    public Labels convertToEntity() {
        Labels label = new Labels();
        label.setLabelsId(this.labelsId);
        label.setLabelsTitle(this.labelsTitle);
        label.setUserId(this.userId);

        return label;
    }

}
