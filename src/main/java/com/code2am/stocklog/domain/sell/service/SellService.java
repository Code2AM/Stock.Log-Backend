package com.code2am.stocklog.domain.sell.service;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.sell.Infra.JournalsRepoForSell;
import com.code2am.stocklog.domain.sell.dao.SellDAO;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.repository.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private SellDAO sellDAO;

    @Autowired
    private JournalsRepoForSell journalsRepo;

    /**
     * 매도 등록
     * */
    public String createSell(Sell sell) {

        Integer journalId = sell.getJournals().getJournalId();

        if(journalsRepo.findById(journalId).isPresent() && journalsRepo.findById(journalId).get().getTotalQuantity() < sell.getSellQuantity()){
            return "매도물량이 보유물량보다 클 수 없습니다.";
        }

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

        // 평균값 등록 로직
        List<SellDTO> sellList = sellDAO.readSellByJournalId(journalId);

        Integer sellSum = 0;

        for (SellDTO sellDTO : sellList) {
            sellSum += sellDTO.getSellPrice();
        }

        Integer sellAvg = sellSum / sellList.size();

        Optional<Journals> updateJournals = journalsRepo.findById(journalId);
        if(updateJournals.isEmpty()){
            return "평균값 등록 실패";
        }

        Journals updateJournalsAvgSell = updateJournals.get();
        updateJournalsAvgSell.setAvgSellPrice(sellAvg);
        updateJournalsAvgSell.setLastedTradeDate(LocalDateTime.now());
        journalsRepo.save(updateJournalsAvgSell);

        return "등록 성공";
    }

    /**
     * 매도 조회
     * */
    public List<SellDTO> readSellByJournalId(Integer journalId) {

        return sellDAO.readSellByJournalId(journalId);
    }

    /**
     * 매도 삭제
     * */
    public String deleteSellBySellId(Integer sellId) {

        Optional<Sell> deleteSell = sellRepository.findById(sellId);
        if(deleteSell.isEmpty()){
            return "삭제 실패";
        }

        Sell sell = deleteSell.get();
        sell.setStatus("N");
        sellRepository.save(sell);

        Integer journalId = deleteSell.get().getJournals().getJournalId();

        // 평균값 등록 로직
        List<SellDTO> sellList = sellDAO.readSellByJournalId(journalId);

        Integer sellSum = 0;

        for (SellDTO sellDTO : sellList) {
            sellSum += sellDTO.getSellPrice();
        }

        Integer sellAvg = sellSum / sellList.size();

        Optional<Journals> updateJournals = journalsRepo.findById(journalId);
        if(updateJournals.isEmpty()){
            return "평균값 등록 실패";
        }

        SellDTO oldSellDate = sellDAO.readLastedDateBySellId(sellId);
        Journals updateJournalsAvgSell = updateJournals.get();
        updateJournalsAvgSell.setLastedTradeDate(oldSellDate.getSellDate());
        updateJournalsAvgSell.setAvgSellPrice(sellAvg);
        journalsRepo.save(updateJournalsAvgSell);

        return "삭제 성공";
    }
}
