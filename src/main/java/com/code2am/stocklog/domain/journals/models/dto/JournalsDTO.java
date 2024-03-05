package com.code2am.stocklog.domain.journals.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalsDTO {

    private String StockName;

    private LocalDateTime JournalDate;

    private Integer StrategyId;

    private Integer buyQuantity;

    private Integer buyPrice;

    private double fee;
}
