package com.code2am.stocklog.domain.journals.models.entity;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.users.models.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
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

    @Column(name = "TOTAL_BUY_QUANTITY")
    private Integer totalBuyQuantity;

    @Column(name = "AVG_SELL_PRICE")
    private Integer avgSellPrice;

    @Column(name = "TOTAL_SELL_QUANTITY")
    private Integer totalSellQuantity;

    @Column(name = "PROFIT")
    private Integer profit;

    @Column(name = "LASTEST_TRADE_DATE")
    private LocalDateTime lastedTradeDate;

    @Column(name = "STRATEGY_ID")
    private Integer strategyId;

    @Column(name = "FEE")
    private double fee;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID")
    private Users user;


    @Builder
    public Journals(Integer journalId, LocalDateTime journalDate, String stockName, Integer totalQuantity, Integer avgBuyPrice, Integer totalBuyQuantity, Integer avgSellPrice, Integer totalSellQuantity, Integer profit, LocalDateTime lastedTradeDate, Integer strategyId, double fee, String status, Users user) {
        this.journalId = journalId;
        this.journalDate = journalDate;
        this.stockName = stockName;
        this.totalQuantity = totalQuantity;
        this.avgBuyPrice = avgBuyPrice;
        this.totalBuyQuantity = totalBuyQuantity;
        this.avgSellPrice = avgSellPrice;
        this.totalSellQuantity = totalSellQuantity;
        this.profit = profit;
        this.lastedTradeDate = lastedTradeDate;
        this.strategyId = strategyId;
        this.fee = fee;
        this.status = status;
        this.user = user;
    }
}
