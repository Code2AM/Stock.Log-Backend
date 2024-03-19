package com.code2am.stocklog.domain.labels.models.dto;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.NotBlank;
@Data
@RequiredArgsConstructor
public class LabelsDTO {
    // PK
    private Integer labelsId;

    // 라벨 제목
    @NotNull(message = "최소 한 글자 이상 입력해주세요")
    private String labelsTitle;

    // 라벨 상태
    private String labelsStatus;

    private Integer userId;

    /* 생성자 */
    public LabelsDTO(int labelsId, String labelsTitle) {
        this.labelsId = labelsId;
        this.labelsTitle = labelsTitle;
    }

    public Labels convertDTOToEntity() {
        Labels labels = new Labels();
        labels.setLabelsId(this.labelsId);
        labels.setLabelsTitle(this.labelsTitle);
        labels.setLabelsStatus(this.labelsStatus);
        labels.setUserId(this.userId);

        return labels;
    }


    /* Entity Converter */
    public Labels convertToEntity() {
        Labels label = Labels.builder()
                .labelsId(this.labelsId)
                .labelsTitle(this.labelsTitle)
                .labelsStatus(this.labelsStatus)
                .userId(this.userId)
                .build();

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
