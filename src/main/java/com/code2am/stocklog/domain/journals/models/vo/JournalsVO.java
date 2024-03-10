package com.code2am.stocklog.domain.journals.models.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalsVO {

    private Integer journalId;

    private LocalDateTime journalDate;

    private String stockName;

    private Integer totalQuantity;

    private Integer avgBuyPrice;

    private Integer avgSellPrice;

    private Integer profit;

    private LocalDateTime lastedTradeDate;

    private double fee;

    private Integer userId;

    private Integer strategyId;

    private String status;

}
