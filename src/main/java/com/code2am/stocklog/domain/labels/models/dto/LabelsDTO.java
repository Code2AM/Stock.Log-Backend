package com.code2am.stocklog.domain.labels.models.dto;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@Data
@RequiredArgsConstructor
public class LabelsDTO {
    // PK
    private Integer labelsId;

    // 라벨 제목
    private String labelsTitle;

    // 라벨 상태
    private String labelsStatus;

    private Integer userId;

    /* 생성자 */
    public LabelsDTO(int labelsId, String labelsTitle) {
        this.labelsId = labelsId;
        this.labelsTitle = labelsTitle;
    }


    /* Entity Converter */
    public Labels convertToEntity() {
        Labels label = new Labels();
        label.setLabelsId(this.labelsId);
        label.setLabelsTitle(this.labelsTitle);
        label.setLabelsStatus(this.labelsStatus);
        label.setUserId(this.userId);

        return label;
    }

    @Builder
    public LabelsDTO(Integer labelsId, String labelsTitle, String labelsStatus, Integer userId) {
        this.labelsId = labelsId;
        this.labelsTitle = labelsTitle;
        this.labelsStatus = labelsStatus;
        this.userId = userId;
    }


}
