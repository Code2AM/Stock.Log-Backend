package com.code2am.stocklog.domain.journals.service;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.journals.dao.JournalsDAO;
import com.code2am.stocklog.domain.journals.infra.BuyRepo;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.journals.models.vo.JournalsVO;
import com.code2am.stocklog.domain.journals.repository.JournalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class JournalsService {

    @Autowired
    private JournalsRepository journalsRepository;

    @Autowired
    private BuyRepo buyRepo;

    @Autowired
    private JournalsDAO journalsDAO;

    /**
     * 매매일지 조회
     * */
    public List<JournalsDTO> readJournalsByUserId(Integer userId) {
        return journalsDAO.readJournalsByUserId(userId);
    }

    /**
     * 매매일지 등록
     */
    public String createJournalsByUserId(JournalsDTO journals, Integer userId) {


        if (!(journals.getFee() >= 0 && journals.getFee() <= 1)) {
            return "수수료는 0 ~ 100% 사이 값만 사용할 수 있습니다.";
        }

        if (journals.getStockName().isEmpty()) {
            return "종목을 선택하지 않았습니다.";
        }

        if (journals.getBuyPrice() <= 0) {
            return "정상적인 매수가가 아닙니다.";
        }

        if (journals.getBuyQuantity() <= 0) {
            return "매수량이 0보다 적습니다.";
        }

        if (journals.getStrategyId() <= 0) {
            return "불가능한 매매전략 값입니다.";
        }

        Journals newJournal = new Journals();
        newJournal.setStockName(journals.getStockName());
        newJournal.setJournalDate(LocalDateTime.now());
        newJournal.setLastedTradeDate(journals.getJournalDate());
        newJournal.setAvgBuyPrice(journals.getBuyPrice());
        newJournal.setTotalQuantity(journals.getBuyQuantity());
        newJournal.setTotalBuyQuantity(journals.getBuyQuantity());
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

    /**
     * 매매일지 삭제
     * */
    public String deleteJournalsByJournalsId(Integer journalId) {

        if(journalId == null){
            return "매매일지 정보가 정상적으로 넘어오지 않았습니다.";
        }

        JournalsVO data = journalsDAO.readJournalsByJournalId(journalId);

        if(Objects.isNull(data)){
            return "조회된 결과가 없습니다.";
        }

        if(data.getStatus().equals("N")){
            return "이미 삭제된 매매일지입니다.";
        }

        Journals delete = new Journals();
        delete.setStockName(data.getStockName());
        delete.setJournalId(data.getJournalId());
        delete.setProfit(data.getProfit());
        delete.setJournalDate(data.getJournalDate());
        delete.setTotalQuantity(data.getTotalQuantity());
        delete.setFee(data.getFee());
        delete.setLastedTradeDate(data.getLastedTradeDate());
        delete.setUserId(data.getUserId());
        delete.setAvgSellPrice(data.getAvgSellPrice());
        delete.setAvgBuyPrice(data.getAvgBuyPrice());
        delete.setStrategyId(data.getStrategyId());
        delete.setStatus("N");

        journalsRepository.save(delete);

        return "삭제 성공";
    }

    /**
     * 매매일지 거래 상태 변경 메소드
     * */
    public String updateJournalsStatusByJournalId(Integer journalId) {

        JournalsVO data = journalsDAO.readJournalsByJournalId(journalId);

        if(Objects.isNull(data)){
            return "없는 매매일지입니다.";
        }

        if(data.getStatus().equals("close")){
            return "이미 닫힌 매매일지입니다.";
        }

        Journals update = new Journals();
        update.setStockName(data.getStockName());
        update.setJournalId(data.getJournalId());
        update.setProfit(data.getProfit());
        update.setJournalDate(data.getJournalDate());
        update.setTotalQuantity(data.getTotalQuantity());
        update.setFee(data.getFee());
        update.setLastedTradeDate(data.getLastedTradeDate());
        update.setUserId(data.getUserId());
        update.setAvgSellPrice(data.getAvgSellPrice());
        update.setAvgBuyPrice(data.getAvgBuyPrice());
        update.setStrategyId(data.getStrategyId());
        update.setStatus("close");

        journalsRepository.save(update);

        return "상태 변경 성공";
    }
}