package com.code2am.stocklog.domain.sell.models.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@RequiredArgsConstructor
public class SellResponseDTO {

    private Integer sellId;

    private LocalDateTime sellDate;

    private Integer sellPrice;

    private Integer sellQuantity;

    private String status;

    private Integer journalId;

    @Builder
    public SellResponseDTO(Integer sellId, LocalDateTime sellDate, Integer sellPrice, Integer sellQuantity, String status, Integer journalId) {
        this.sellId = sellId;
        this.sellDate = sellDate;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
        this.status = status;
        this.journalId = journalId;
    }
}