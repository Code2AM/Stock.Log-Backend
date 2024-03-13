package com.code2am.stocklog.domain.labels.models.entity;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
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
}
