package com.code2am.stocklog.domain.labels.models.entity;

import jakarta.persistence.*;

@Entity
public class Labels {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer LabelsId;
}
