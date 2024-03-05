package com.code2am.stocklog.domain.sell.dao;

import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SellDAO {
    List<SellDTO> readSellByJournalId(Integer journalId);

    SellDTO readLastedDateBySellId(Integer sellId);
}
