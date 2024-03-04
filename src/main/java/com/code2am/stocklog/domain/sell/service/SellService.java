package com.code2am.stocklog.domain.sell.service;

import com.code2am.stocklog.domain.sell.dao.SellDAO;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.repository.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private SellDAO sellDAO;

    /**
     * 매도 등록
     * */
    public String createSell(Sell sell) {

        Integer journalId = sell.getJournals().getJournalId();

        if(journalId <= 0){
            return "존재하지 않는 매매일지입니다.";
        }

        if(sell.getSellPrice() <= 0){
            return "매도가는 0 이하일 수 없습니다.";
        }

        if(sell.getSellQuantity() <= 0){
            return "매도량이 0 이하일 수 없습니다.";
        }

        sell.setStatus("Y");
        sellRepository.save(sell);

        return "등록 성공";
    }

    public List<SellDTO> readSellByJournalId(Integer journalId) {

        return sellDAO.readSellByJournalId(journalId);
    }
}
