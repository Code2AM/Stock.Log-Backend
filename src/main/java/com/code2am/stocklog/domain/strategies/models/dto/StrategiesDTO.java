package com.code2am.stocklog.domain.strategies.models.dto;

import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StrategiesDTO {

    private Integer strategyId;

    @NotBlank(message = "매매일지의 이름은 비어있을 수 없습니다.")
    private String strategyName;

    private String strategyStatus;



    /* Entity Converter */
    public Strategies convertToEntity() {
        Strategies strategies = new Strategies();
        strategies.setStrategyId(this.strategyId);
        strategies.setStrategyName(this.strategyName);
        strategies.setStrategyStatus(this.strategyStatus);

        return strategies;
    }

    /* Builder */
    @Builder
    public StrategiesDTO(Integer strategyId, String strategyName, String strategyStatus) {
        this.strategyId = strategyId;
        this.strategyName = strategyName;
        this.strategyStatus = strategyStatus;
    }


    public StrategiesDTO(int strategyId, String strategyName) {
        this.strategyId = strategyId;
        this.strategyName = strategyName;
    }
}
