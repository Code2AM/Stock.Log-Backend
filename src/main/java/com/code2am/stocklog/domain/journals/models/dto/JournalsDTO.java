package com.code2am.stocklog.domain.journals.models.dto;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalsDTO {

    private Integer journalId;

    private String stockName;

    private LocalDateTime journalDate;

    private Integer StrategyId;

    private Integer buyQuantity;

    private Integer buyPrice;

    private double fee;


}
