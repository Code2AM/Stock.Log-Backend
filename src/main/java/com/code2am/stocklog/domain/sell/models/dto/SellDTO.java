package com.code2am.stocklog.domain.sell.models.dto;


import com.code2am.stocklog.domain.sell.models.entity.Sell;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SellDTO {

    private Integer sellId;

    private LocalDateTime sellDate;

    private Integer sellPrice;

    private Integer sellQuantity;

    private String status;

    /* Entity Converter */
    public Sell convertToEntity() {
        Sell sell = new Sell();
        sell.setSellId(this.sellId);
        sell.setSellDate(this.sellDate);
        sell.setSellPrice(this.sellPrice);
        sell.setSellQuantity(this.sellQuantity);
        sell.setStatus(this.status);

        return sell;
    }
}
