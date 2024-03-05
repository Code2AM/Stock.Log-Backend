package com.code2am.stocklog.domain.buy.dao;

import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuyDAO {

    List<BuyDTO> readBuyByJournalId(Integer journalId);

    BuyDTO readLastedBuyDateByBuyId(Integer buyId);

    Integer readBuyQuantityByJournalId(Integer journalId);
}
