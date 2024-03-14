package com.code2am.stocklog.domain.labels.models.entity;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "TBL_LABELS")
public class Labels {

    @Id
    @Column(name = "LABELS_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer labelsId;

    @Column(name = "LABELS_TITLE")
    private String labelsTitle;

    @Column(name = "LABELS_STATUS")
    private String labelsStatus;

    @JoinColumn(name = "USER_ID")
    private Integer userId;

    @OneToMany(mappedBy = "labels")
    private List<Notes> notes;

    /* DTO Converter */
    public LabelsDTO convertToDTO() {
        LabelsDTO label = new LabelsDTO();
        label.setLabelsId(this.labelsId);
        label.setLabelsTitle(this.labelsTitle);
        label.setUserId(this.userId);

        return label;
    }

    @Builder
    public Labels(Integer labelsId, String labelsTitle, String labelsStatus, Integer userId, List<Notes> notes) {
        this.labelsId = labelsId;
        this.labelsTitle = labelsTitle;
        this.labelsStatus = labelsStatus;
        this.userId = userId;
        this.notes = notes;
    }
}
