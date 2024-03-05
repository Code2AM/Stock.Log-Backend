package com.code2am.stocklog.domain.sell.models.entity;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_SELL")
@Data
public class Sell {

    @Id
    @Column(name = "SELL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellId;

    @Column(name = "SELL_DATE")
    private LocalDateTime sellDate;

    @Column(name = "SELL_PRICE")
    private Integer sellPrice;

    @Column(name = "SELL_QUANTITY")
    private Integer sellQuantity;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "JOURNAL_ID")
    private Journals journals;

}
