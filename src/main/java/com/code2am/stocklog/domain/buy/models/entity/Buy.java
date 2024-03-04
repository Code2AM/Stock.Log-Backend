package com.code2am.stocklog.domain.buy.models.entity;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TBL_BUY")
public class Buy {

    @Id
    @Column(name = "BUY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buyId;

    @Column(name = "BUY_DATE")
    private LocalDateTime buyDate;

    @Column(name = "BUY_QUANTITY")
    private Integer buyQuantity;

    @Column(name = "BUY_PRICE")
    private Integer buyPrice;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "JOURNAL_ID")
    private Journals journals;
}
