package com.code2am.stocklog.domain.sell.models.dto;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class SellRequestDTO {

    private Integer sellId;

    @NotNull(message = "매도 날짜는 비어있을 수 없습니다")
    private LocalDateTime sellDate;

    @NotNull(message = "매도 가격은 비어있을 수 없습니다")
    @Min(value = 0, message = "매도가는 0 이하일 수 없습니다.")
    private Integer sellPrice;

    @NotNull(message = "매도 물량은 비어있을 수 없습니다")
    @Min(value = 0, message = "매도량이 0 이하일 수 없습니다.")
    private Integer sellQuantity;

    private String status;

    // journalId 를 추가
    private Integer journalId;

    /* Entity Converter */
    public Sell convertToEntity() {

        return Sell.builder()
                .sellId(this.sellId)
                .sellDate(this.sellDate)
                .sellPrice(this.sellPrice)
                .sellQuantity(this.sellQuantity)
                .status(this.status)
                .journal(
                        Journals.builder()
                                .journalId(this.journalId)
                                .build()
                )
                .build();
    }

    @Builder
    public SellRequestDTO(Integer sellId, LocalDateTime sellDate, Integer sellPrice, Integer sellQuantity, String status, Integer journalId) {
        this.sellId = sellId;
        this.sellDate = sellDate;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
        this.status = status;
        this.journalId = journalId;
    }
}
