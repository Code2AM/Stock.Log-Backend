package com.code2am.stocklog.domain.buy.models.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyDTO {

    private Integer buyId;

    private LocalDateTime buyDate;

    private Integer buyQuantity;

    private Integer buyPrice;

    private String status;
}
