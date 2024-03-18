package com.code2am.stocklog.domain.journals.models.dto;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JournalsDTO {

    private Integer journalId;

    @NotNull(message = "종목이 선택되지 않았습니다.")
    private String stockName;

    private LocalDateTime journalDate;

    private Integer StrategyId;

    private Integer buyQuantity;

    private Integer buyPrice;

    private double fee;


}
