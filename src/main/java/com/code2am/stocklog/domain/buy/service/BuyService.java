package com.code2am.stocklog.domain.buy.service;

import com.code2am.stocklog.domain.buy.infra.JournalsRepoForBuy;
import com.code2am.stocklog.domain.buy.dao.BuyDAO;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.repository.BuyRepository;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BuyService {

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyDAO buyDAO;

    @Autowired
    private JournalsRepoForBuy journalsRepoForBuy;

    /**
     * 매수 등록
     * */
    public String createBuy(Buy buy) {

        Integer journalId = buy.getJournals().getJournalId();

        if(journalId <= 0){
            return "존재하지 않는 매매일지입니다.";
        }

        if(buy.getBuyPrice() <= 0){
            return "매수가는 0 이하일 수 없습니다.";
        }

        if(buy.getBuyQuantity() <= 0){
            return "매수량이 0 이하일 수 없습니다.";
        }

        buy.setStatus("Y");
        buyRepository.save(buy);

        // 평균값 등록 로직
        List<BuyDTO> buyList = buyDAO.readBuyByJournalId(journalId);

        Integer buySum = 0;

        for (BuyDTO buyDTO : buyList) {
            buySum += buyDTO.getBuyPrice();
        }

        Integer buyAvg = buySum / buyList.size();

        Optional<Journals> updateJournals =  journalsRepoForBuy.findById(journalId);
        if(updateJournals.isEmpty()){
            return "평균값 등록 실패";
        }

        Journals updateJournalsAvgBuy = updateJournals.get();
        updateJournalsAvgBuy.setAvgBuyPrice(buyAvg);
        updateJournalsAvgBuy.setLastedTradeDate(LocalDateTime.now());
        journalsRepoForBuy.save(updateJournalsAvgBuy);

        return "등록 성공";
    }

    /**
     * 매수 삭제
     * */
    public String deleteBuyByBuyId(Integer buyId) {

        Optional<Buy> deleteBuy = buyRepository.findById(buyId);
        if(deleteBuy.isEmpty()){
            return "삭제 실패";
        }
        Buy buy = deleteBuy.get();
        buy.setStatus("N");
        buyRepository.save(buy);

        // 평균값 등록 로직

        Integer journalId = deleteBuy.get().getJournals().getJournalId();
        List<BuyDTO> buyList = buyDAO.readBuyByJournalId(journalId);

        Integer buySum = 0;

        for (BuyDTO buyDTO : buyList) {
            buySum += buyDTO.getBuyPrice();
        }

        Integer buyAvg = buySum / buyList.size();

        Optional<Journals> updateJournals =  journalsRepoForBuy.findById(journalId);
        if(updateJournals.isEmpty()){
            return "평균값 등록 실패";
        }

        Journals updateJournalsAvgBuy = updateJournals.get();
        updateJournalsAvgBuy.setAvgBuyPrice(buyAvg);
        updateJournalsAvgBuy.setLastedTradeDate(LocalDateTime.now());
        journalsRepoForBuy.save(updateJournalsAvgBuy);

        return "삭제 성공";
    }

    /**
     * 매수 조회
     * */
    public List<BuyDTO> readBuyByJournalId(Integer journalId) {

        return buyDAO.readBuyByJournalId(journalId);
    }
}
