package com.code2am.stocklog.domain.sell.service;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.sell.Infra.JournalsRepoForSell;
import com.code2am.stocklog.domain.sell.dao.SellDAO;
import com.code2am.stocklog.domain.sell.handler.exception.SellException;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellRequestDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.repository.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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
    public String createSell(SellRequestDTO sellRequestDTO) {

        // 받은 dto를 entity로 변환
        Sell sell = sellRequestDTO.convertToEntity();

        // 해당 매도의 매매일지 id를 받는다
        Integer journalId = sell.getJournal().getJournalId();

        // 해당 매도의 매매일지를 불러온다
        Journals journal;

        try {
            journal = journalsRepo.findById(journalId).get();
        }
        catch (NoSuchElementException e){
            throw new SellException("존재하지 않는 매매일지입니다.");
        }


        if( journal.getTotalQuantity() < sell.getSellQuantity() ){
            throw new SellException("매도물량이 보유물량보다 클 수 없습니다.");
        }

        /* Sell 저장 */

        // 매도 entity에 불러온 journal을 담아서 최신화
        sell.setJournal(journal);

        // 매도의 상태 "Y"
        sell.setStatus("Y");

        // 등록
        Sell saveResult = sellRepository.save(sell);

        // 등록 됐는지 확인
        System.out.println(saveResult);



        /* Journal 저장 */

        // 해당하는 매매일지에 등록할 평균값을 구한다
        Integer averageSellPrice = getAverageSellPrice(journal);

        journal.setAvgSellPrice(averageSellPrice);

        // 해당하는 매매일지에 등록할 profit을 구한다
        Integer profit = getProfit(journal);

        journal.setProfit(profit);

        // 저장하기전 값이 제대로 들어갔는지 확인
        System.out.println(journal);

        // 최신화된 매매일지를 저장
        journalsRepo.save(journal);

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
    public String deleteSellBySellId(SellRequestDTO sellRequestDTO) {

        // 받은 dto를 entity로 변환
        Sell deleteSell = sellRequestDTO.convertToEntity();

        // 해당 매도의 매매일지 id를 받는다
        Integer journalId = deleteSell.getJournal().getJournalId();

        // 해당 매도의 매매일지를 불러온다
        Journals journal;

        try {
            journal = journalsRepo.findById(journalId).get();
        }
        catch (NoSuchElementException e){
            throw new SellException("존재하지 않는 매매일지입니다.");
        }

        // 매도 entity에 불러온 journal을 담아서 최신화
        deleteSell.setJournal(journal);

        /* Sell 삭제 */
        deleteSell.setStatus("N");

        sellRepository.save(deleteSell);

        /* Journal 저장 */

        // 해당하는 매매일지에 등록할 평균값을 구한다
        Integer averageSellPrice = getAverageSellPrice(journal);

        journal.setAvgSellPrice(averageSellPrice);

        // 해당하는 매매일지에 등록할 profit을 구한다
        Integer profit = getProfit(journal);

        journal.setProfit(profit);

        // 저장하기전 값이 제대로 들어갔는지 확인
        System.out.println(journal);

        // 최신화된 매매일지를 저장
        journalsRepo.save(journal);

        return "삭제 성공";
    }



    /* 매도 평균값 최신화 */
    public Integer getAverageSellPrice(Journals journal){
        // journaId 기반 모든 Sell을 불러온다
        List<Sell> sellList = sellRepository.findAllByJournal(journal);

        // Stream API를 사용하여 평균 값 계산
        double averagePrice = sellList.stream()
                .mapToInt(Sell::getSellPrice) // Sell 객체의 sellPrice 필드 추출
                .average() // 평균 계산
                .getAsDouble(); // Optional 객체의 값을 double 형으로 변환 // sellList 가 없는 경우 NoSuchElement 에러 반환

        return (int) averagePrice;
    }


    /* profit을 구함 */
    public Integer getProfit(Journals journal){

        /* profit = 매도총액 - 매수총액 */

        // 매도총액
        Integer totalSellPrice = journal.getAvgSellPrice() * journal.getTotalSellQuantity();

        // 매수총액
        Integer totalBuyPrice = journal.getAvgBuyPrice() * journal.getTotalBuyQuantity();

        // profit 에서 수수료를 뺀다
        double fee = journal.getFee();

        double profit = (totalSellPrice - totalBuyPrice) * (1 - fee);

        return (int) profit;
    }

}
