package com.code2am.stocklog.domain.journals.models.entity;

import com.code2am.stocklog.domain.users.models.entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TBL_JOURNALS")
public class Journals {

    @Id
    @Column(name = "JOURNAL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer journalId;

    @Column(name = "JOURNAL_DATE")
    private LocalDateTime journalDate;

    @Column(name = "STOCK_NAME")
    private String stockName;

    @Column(name = "TOTAL_QUANTITY")
    private Integer totalQuantity;

    @Column(name = "AVG_BUY_PRICE")
    private Integer avgBuyPrice;

    @Column(name = "AVG_SELL_PRICE")
    private Integer avgSellPrice;

    @Column(name = "PROFIT")
    private Integer profit;

    @Column(name = "LASTEST_TRADE_DATE")
    private LocalDateTime lastedTradeDate;

    @Column(name = "STRATEGY_ID")
    private Integer strategyId;

    @Column(name = "FEE")
    private double fee;

    @JoinColumn(name = "USER_ID")
    private Integer userId;

}
