package com.code2am.stocklog.domain.strategies.models.entity;

import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "TBL_STRATEGIES")
@Data
public class Strategies {

    @Id
    @Column(name = "STRATEGY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer strategyId;

    @Column(name = "STRATEGY_NAME")
    public String strategyName;

    @Column(name = "STRATEGY_STATUS")
    public String strategyStatus;

    /* DTO Converter */
    public StrategiesDTO convertToDTO() {
        StrategiesDTO strategies = new StrategiesDTO();
        strategies.setStrategyId(this.strategyId);
        strategies.setStrategyName(this.strategyName);
        strategies.setStrategyStatus(this.strategyStatus);

        return strategies;
    }

}
