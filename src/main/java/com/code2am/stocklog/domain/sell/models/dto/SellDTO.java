package com.code2am.stocklog.domain.sell.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SellDTO {

    private Integer sellId;

    private LocalDateTime sellDate;

    private Integer sellPrice;

    private Integer sellQuantity;

    private String status;
}
