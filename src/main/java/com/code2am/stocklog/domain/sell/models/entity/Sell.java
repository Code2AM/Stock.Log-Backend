package com.code2am.stocklog.domain.sell.models.entity;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellResponseDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TBL_SELL")
@Data
@RequiredArgsConstructor
public class Sell {

    @Id
    @Column(name = "SELL_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sellId;

    @Column(name = "SELL_DATE")
    private LocalDateTime sellDate;

    @Column(name = "SELL_PRICE")
    private Integer sellPrice;

    @Column(name = "SELL_QUANTITY")
    private Integer sellQuantity;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "JOURNAL_ID")
    private Journals journal;

    /* DTO Converter */
    public SellResponseDTO convertToDTO() {

        return SellResponseDTO.builder()
                .sellId(this.sellId)
                .sellDate(this.sellDate)
                .sellPrice(this.sellPrice)
                .sellQuantity(this.sellQuantity)
                .status(this.status)
                .journalId(this.journal.getJournalId())
                .build();
    }

    /* Builder */
    @Builder
    public Sell(Integer sellId, LocalDateTime sellDate, Integer sellPrice, Integer sellQuantity, String status, Journals journal) {
        this.sellId = sellId;
        this.sellDate = sellDate;
        this.sellPrice = sellPrice;
        this.sellQuantity = sellQuantity;
        this.status = status;
        this.journal = journal;
    }

}
