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
import java.util.Objects;
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
        updateJournalsAvgSell.setTotalQuantity(updateJournalsAvgSell.getTotalQuantity() - sell.getSellQuantity()); // 보유총량 빼기
        journalsRepo.save(updateJournalsAvgSell);

        // 손익 계산 로직
        Optional<Journals> profitCal = journalsRepo.findById(journalId);
        if(profitCal.isEmpty()){
            return "손익계산 실패";
        }

        Integer avgBuyPrice = profitCal.get().getAvgBuyPrice();
        Integer avgSellPrice = profitCal.get().getAvgSellPrice();
        Integer totalQuantity = profitCal.get().getTotalQuantity();
        double fee = profitCal.get().getFee();

        profitCal.get().setProfit((int) ((avgSellPrice - avgBuyPrice) * totalQuantity * (1 - fee)));
        System.out.println(profitCal.get().getProfit());
        Journals updateProfit = profitCal.get();

        journalsRepo.save(updateProfit);

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
        } else if (deleteSell.get().getStatus().equals("N")) {
            return "이미 삭제된 매도기록입니다.";
        }

        Sell sell = deleteSell.get();
        sell.setStatus("N");

        SellDTO oldSellDate = sellDAO.readLastedDateBySellId(sellId); // 값을 미리 빼둔다.

        Integer plusValue = sell.getSellQuantity(); // 값을 미리 빼둔다.
        sellRepository.save(sell);

        Integer journalId = deleteSell.get().getJournals().getJournalId();

        // 평균값 등록 로직
        List<SellDTO> sellList = sellDAO.readSellByJournalId(journalId);
        Integer sellSum = 0;

        if(sellList.isEmpty()){
            sellSum = 0;
        }else {
            for (SellDTO sellDTO : sellList) {
                sellSum += sellDTO.getSellPrice();
            }
        }

        int sellAvg = 0;

        if(sellSum != 0){
            sellAvg = sellSum / sellList.size();
        }

        Optional<Journals> updateJournals = journalsRepo.findById(journalId);
        if(updateJournals.isEmpty()){
            return "평균값 등록 실패";
        }

        Journals updateJournalsAvgSell = updateJournals.get();
        if(!(oldSellDate == null)){
            updateJournalsAvgSell.setLastedTradeDate(oldSellDate.getSellDate());
        }
        updateJournalsAvgSell.setAvgSellPrice(sellAvg);
        updateJournalsAvgSell.setTotalQuantity(updateJournalsAvgSell.getTotalQuantity() + plusValue); // 보유총량 더하기
        journalsRepo.save(updateJournalsAvgSell);

        // 손익 계산 로직
        Optional<Journals> profitCal = journalsRepo.findById(journalId);
        if(profitCal.isEmpty()){
            return "손익계산 실패";
        }

        Integer avgBuyPrice = profitCal.get().getAvgBuyPrice();
        Integer avgSellPrice = profitCal.get().getAvgSellPrice();
        Integer totalQuantity = profitCal.get().getTotalQuantity();
        double fee = profitCal.get().getFee();

        if(avgSellPrice != 0){
            profitCal.get().setProfit((int) ((avgSellPrice - avgBuyPrice) * totalQuantity * (1 - fee)));
        }else {
            profitCal.get().setProfit(0);
        }

        Journals updateProfit = profitCal.get();

        journalsRepo.save(updateProfit);

        return "삭제 성공";
    }

}
