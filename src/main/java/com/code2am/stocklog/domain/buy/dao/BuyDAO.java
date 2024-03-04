package com.code2am.stocklog.domain.buy.dao;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BuyDAO {
    List<Buy> readBuyListByJournalId(Integer journalId);
}
