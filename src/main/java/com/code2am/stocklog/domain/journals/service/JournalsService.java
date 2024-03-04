package com.code2am.stocklog.domain.journals.service;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.journals.infra.BuyRepo;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.journals.repository.JournalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class JournalsService {

    @Autowired
    private JournalsRepository journalsRepository;

    @Autowired
    private BuyRepo buyRepo;


    public String createJournalsByUserId(JournalsDTO journals, Integer userId) {

        if(journals.getFee() <= 0){
            return "수수료가 0 이하 값이 될 수는 없습니다.";
        }

        if(journals.getStockName().equals("")){
            return "종목을 선택하지 않았습니다.";
        }

        if(journals.getBuyPrice() <= 0){
            return "정상적인 매수가가 아닙니다.";
        }

        if(journals.getBuyQuantity() <= 0){
            return "매매주가 없습니다.";
        }

        if(journals.getStrategyId() < 0){
            return "불가능한 매매전략 값입니다.";
        }

        Journals newJournal = new Journals();
        newJournal.setStockName(journals.getStockName());
        newJournal.setJournalDate(LocalDateTime.now());
        newJournal.setLastedTradeDate(journals.getJournalDate());
        newJournal.setAvgBuyPrice(journals.getBuyPrice());
        newJournal.setTotalQuantity(journals.getBuyQuantity());
        newJournal.setAvgSellPrice(0);
        newJournal.setStrategyId(journals.getStrategyId());
        newJournal.setFee(journals.getFee());
        newJournal.setProfit(0);
        newJournal.setStatus("open");
        newJournal.setUserId(userId);

        Buy newBuy = new Buy();
        newBuy.setJournals(newJournal);
        newBuy.setBuyPrice(journals.getBuyPrice());
        newBuy.setBuyQuantity(journals.getBuyQuantity());
        newBuy.setBuyDate(journals.getJournalDate());
        newBuy.setStatus("Y");

        journalsRepository.save(newJournal);
        buyRepo.save(newBuy);

        return "등록 성공";
    }
}