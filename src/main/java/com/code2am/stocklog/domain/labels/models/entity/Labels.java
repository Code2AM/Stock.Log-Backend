package com.code2am.stocklog.domain.labels.models.entity;

import com.code2am.stocklog.domain.notes.models.entity.Notes;
import jakarta.persistence.*;
import lombok.Data;

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

//    @OneToOne
//    @JoinColumn(name = "NOTE_ID")
//    private Notes notes;
}
