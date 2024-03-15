package com.code2am.stocklog.domain.buy.models.entity;

import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "TBL_BUY")
public class Buy {

    @Id
    @Column(name = "BUY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer buyId;

    @Column(name = "BUY_DATE")
    private LocalDateTime buyDate;

    @Column(name = "BUY_QUANTITY")
    private Integer buyQuantity;

    @Column(name = "BUY_PRICE")
    private Integer buyPrice;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "JOURNAL_ID")
    private Journals journals;


    /* DTO Converter */
    public BuyDTO convertToDTO() {

        return BuyDTO.builder()
                .buyId(buyId)
                .buyPrice(buyPrice)
                .buyQuantity(buyQuantity)
                .status(status)
                .build();
    }

    /* Builder */
    @Builder
    public Buy(Integer buyId, LocalDateTime buyDate, Integer buyQuantity, Integer buyPrice, String status, Journals journals) {
        this.buyId = buyId;
        this.buyDate = buyDate;
        this.buyQuantity = buyQuantity;
        this.buyPrice = buyPrice;
        this.status = status;
        this.journals = journals;
    }



}
