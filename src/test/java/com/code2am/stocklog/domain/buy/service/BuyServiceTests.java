package com.code2am.stocklog.domain.buy.service;

import static org.mockito.BDDMockito.given;

import com.code2am.stocklog.domain.buy.dao.BuyDAO;
import com.code2am.stocklog.domain.buy.infra.JournalsRepoForBuy;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.repository.BuyRepository;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BuyServiceTests {

    @InjectMocks
    BuyService buyService;

    @Mock
    BuyDAO buyDAO;

    @Mock
    BuyRepository buyRepository;

    @Mock
    JournalsRepoForBuy journalsRepoForBuy;

    @Test
    void 매개변수가_정상적으로_넘어오지_않은_경우(){
        Journals journals = new Journals();
        journals.setJournalId(-1);

        Buy buy = new Buy();
        buy.setJournals(journals);

        String result = buyService.createBuy(buy);

        Assertions.assertEquals("존재하지 않는 매매일지입니다.", result);
    }

    @Test
    void 매수가가_0_이하인_경우(){
        Journals journals = new Journals();
        journals.setJournalId(100);

        Buy buy = new Buy();
        buy.setJournals(journals);
        buy.setBuyPrice(0);

        String result = buyService.createBuy(buy);

        Assertions.assertEquals("매수가는 0 이하일 수 없습니다.", result);
    }

    @Test
    void 매수량이_0_이하인_경우(){
        Journals journals = new Journals();
        journals.setJournalId(100);

        Buy buy = new Buy();
        buy.setJournals(journals);
        buy.setBuyPrice(100);
        buy.setBuyQuantity(0);

        String result = buyService.createBuy(buy);

        Assertions.assertEquals("매수량이 0 이하일 수 없습니다.", result);
    }

    @Test
    void 매매일지_정보가_없는_경우(){

        Buy buy = new Buy();
        buy.setBuyPrice(100);
        buy.setBuyQuantity(0);

        String result = buyService.createBuy(buy);

        Assertions.assertEquals("매매일지 정보가 없습니다.", result);

    }

    // buyRepository에 정상적으로 등록이 되지 않은 경우 -> ?

    @Test
    void 삭제할_매수기록이_없는_경우() {

        Buy buy = new Buy();
        buy.setBuyId(1);

        Assertions.assertEquals(buyRepository.findById(buy.getBuyId()), Optional.empty());
    }

    @Test
    void 매수_조회() {

        Integer journalId = 10;

        BuyDTO buy = new BuyDTO();
        buy.setBuyId(1);
        buy.setBuyPrice(100);
        buy.setBuyQuantity(10);
        buy.setBuyDate(LocalDateTime.now());
        buy.setStatus("Y");

        List<BuyDTO> list = new ArrayList<>();
        list.add(buy);

        given(buyDAO.readBuyByJournalId(journalId)).willReturn(list);

        List<BuyDTO> result = buyDAO.readBuyByJournalId(journalId);

        Assertions.assertEquals(result.get(0), buy);
    }

    @Test
    void 매수_기록이_없는_경우(){

        Integer journalId = 10;

        BuyDTO buy = new BuyDTO();
        buy.setBuyId(1);
        buy.setBuyPrice(100);
        buy.setBuyQuantity(10);
        buy.setBuyDate(LocalDateTime.now());
        buy.setStatus("Y");

        List<BuyDTO> list = new ArrayList<>();

        given(buyDAO.readBuyByJournalId(journalId)).willReturn(list);

        List<BuyDTO> result = buyDAO.readBuyByJournalId(journalId);

        Assertions.assertEquals(result, list);
    }

    /* readBuyQuantityByJournalId */
    @Test
    void 총_매수_물량_조회_성공() {

        Integer journalId = 1;

        BuyDTO buy = new BuyDTO();
        buy.setBuyId(1);
        buy.setBuyPrice(100);
        buy.setBuyQuantity(10);
        buy.setBuyDate(LocalDateTime.now());
        buy.setStatus("Y");

        BuyDTO buy1 = new BuyDTO();
        buy1.setBuyId(1);
        buy1.setBuyPrice(100);
        buy1.setBuyQuantity(10);
        buy1.setBuyDate(LocalDateTime.now());
        buy1.setStatus("Y");

        given(buyDAO.readBuyQuantityByJournalId(journalId)).willReturn(buy.getBuyQuantity()+buy1.getBuyQuantity());

        Integer qtt = buyDAO.readBuyQuantityByJournalId(1);

        Assertions.assertEquals(qtt, 20);
    }

    @Test
    void 총_매수_물량_조회_실패(){

        Integer qtt = buyDAO.readBuyQuantityByJournalId(null);

        Assertions.assertEquals(qtt, 0);
    }

}