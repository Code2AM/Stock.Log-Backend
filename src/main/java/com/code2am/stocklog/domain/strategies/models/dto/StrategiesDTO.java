package com.code2am.stocklog.domain.strategies.models.dto;

import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import lombok.Data;

@Data
public class StrategiesDTO {

    private Integer strategyId;

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
}
