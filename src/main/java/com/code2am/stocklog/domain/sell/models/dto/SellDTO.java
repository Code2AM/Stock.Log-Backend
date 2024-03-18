package com.code2am.stocklog.domain.sell.models.dto;


import com.code2am.stocklog.domain.sell.models.entity.Sell;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class SellDTO {

    private Integer sellId;

    private LocalDateTime sellDate;

    private Integer sellPrice;

    private Integer sellQuantity;

    private String status;

    private Integer journalId;

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

    @Builder

    public SellDTO(Integer sellId, LocalDateTime sellDate, Integer sellPrice, Integer sellQuantity, String status, Integer journalId) {
        this.sellId = sellId;
        this.sellDate = sellDate;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
        this.status = status;
        this.journalId = journalId;
    }
}
