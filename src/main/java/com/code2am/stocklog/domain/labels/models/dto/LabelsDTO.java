package com.code2am.stocklog.domain.labels.models.dto;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LabelsDTO {
    // PK
    private Integer labelsId;

    // 라벨 제목
    @NotBlank(message = "최소 한 글자 이상 입력해주세요")
    private String labelsTitle;

    // 라벨 상태
    private String labelsStatus;

    private Integer userId;

    /* Entity Converter */
    public Labels convertToEntity() {
        Labels label = new Labels();
        label.setLabelsId(this.labelsId);
        label.setLabelsTitle(this.labelsTitle);
        label.setLabelsStatus(this.labelsStatus);
        label.setUserId(this.userId);

        return label;
    }

}
