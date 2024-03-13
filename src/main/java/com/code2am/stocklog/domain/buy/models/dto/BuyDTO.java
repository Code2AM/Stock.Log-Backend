package com.code2am.stocklog.domain.buy.models.dto;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyDTO {

    private Integer buyId;

    private LocalDateTime buyDate;

    private Integer buyQuantity;

    private Integer buyPrice;

    private String status;

    public BuyDTO() {

    }

    /* Entity Converter */
    public Buy convertToEntity() {
        Buy buy = new Buy();
        buy.setBuyId(this.buyId);
        buy.setBuyPrice(this.buyPrice);
        buy.setBuyQuantity(this.buyQuantity);
        buy.setBuyPrice(this.buyPrice);
        buy.setStatus(this.status);

        return buy;
    }

    /* Builder */
    @Builder
    public BuyDTO(Integer buyId, LocalDateTime buyDate, Integer buyQuantity, Integer buyPrice, String status) {
        this.buyId = buyId;
        this.buyDate = buyDate;
        this.buyQuantity = buyQuantity;
        this.buyPrice = buyPrice;
        this.status = status;
    }


}
