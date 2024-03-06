package com.code2am.stocklog.domain.stocks.models.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_STOCKS")
public class Stocks {
    @Id
    @Column(name = "STOCK_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stockId;

    @Column(name = "STOCK_CODE")
    private String isinCd;

    @Column(name = "STOCK_NAME")
    private String itmsNm;
}
