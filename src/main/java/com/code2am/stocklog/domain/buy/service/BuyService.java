package com.code2am.stocklog.domain.buy.service;

import com.code2am.stocklog.domain.buy.infra.JournalsRepo;
import com.code2am.stocklog.domain.buy.dao.BuyDAO;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.repository.BuyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuyService {

    @Autowired
    private BuyRepository buyRepository;

    @Autowired
    private BuyDAO buyDAO;

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

        return "등록 성공";
    }

    /**
     * 매수 조회
     * */
    public List<BuyDTO> readBuyByUserId(Integer userId) {

        return buyDAO.readBuyByUserId(userId);
    }

    /**
     * 매수 삭제
     * */
    public String deleteBuyByBuyId(Integer buyId) {

        Optional<Buy> deleteBuy = buyRepository.findById(buyId);
        if(deleteBuy.isPresent()){
            Buy buy = deleteBuy.get();
            buy.setStatus("N");
            buyRepository.save(buy);
            return "삭제 성공";
        }

        return "삭제 실패";
    }
}
