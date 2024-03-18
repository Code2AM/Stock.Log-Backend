package com.code2am.stocklog.domain.sell.service;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.sell.Infra.JournalsRepoForSell;
import com.code2am.stocklog.domain.sell.dao.SellDAO;
import com.code2am.stocklog.domain.sell.handler.exception.SellException;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellRequestDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.repository.SellRepository;
import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;


@ExtendWith(MockitoExtension.class)
class SellServiceTest {

    @InjectMocks
    private SellService sellService;

    @Mock
    private SellRepository sellRepository;

    @Mock
    private SellDAO sellDAO;

    @Mock
    private JournalsRepoForSell journalsRepoForSell;



    // 성공
    @Test
    void createSell_성공1() {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(100)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("Y");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        // stub
        given(journalsRepoForSell.findById(anyInt())).willReturn(Optional.ofNullable(journal));
        given(sellRepository.save(any(Sell.class))).willReturn(sell);
        given(sellRepository.findAllByJournal(journal)).willReturn(sellList);


        // when
        String result = sellService.createSell(sellRequestDTO);

        assertEquals(result , "등록 성공");

    }


    // 매매일지가 존재하지 안흔 경우
    @Test
    void createSell_실패1() {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(100)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("Y");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        // stub
        given(journalsRepoForSell.findById(anyInt())).willThrow(new NoSuchElementException());


        // when & then

        assertThrows(SellException.class, () -> {
            sellService.createSell(sellRequestDTO);
        });
    }

    // 매도물량이 보유량보다 큰 경우
    @Test
    void createSell_실패2() {

        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(200)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("Y");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        // stub
        given(journalsRepoForSell.findById(anyInt())).willReturn(Optional.ofNullable(journal));


        // when & then

        assertThrows(SellException.class, () -> {
            sellService.createSell(sellRequestDTO);
        });
    }

    // jpa 에러
    @Test
    @DisplayName("데이터베이스 저장 실패, jpa 관련 문제")
    void createSell_실패3() {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(200)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("Y");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(journalsRepoForSell.findById(anyInt())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });


        // when & then

        assertThrows(DataAccessException.class, () -> {
            sellService.createSell(sellRequestDTO);
        });
    }







    // 성공 - 리스트 반환
    @Test
    void readSellByJournalId_성공1() {
        // given
        List<SellDTO> expectedSellDTOList = Arrays.asList(
                SellDTO.builder()
                        .sellId(1)
                        .sellDate(LocalDateTime.now())
                        .sellPrice(500)
                        .sellQuantity(10)
                        .journalId(1)
                        .build(),
                SellDTO.builder()
                        .sellId(2)
                        .sellDate(LocalDateTime.now())
                        .sellPrice(800)
                        .sellQuantity(20)
                        .journalId(1)
                        .build()
        );

        // stub
        given(sellDAO.readSellByJournalId(anyInt())).willReturn(expectedSellDTOList);

        // when
        List<SellDTO> result = sellService.readSellByJournalId(1);

        // then
        assertEquals(expectedSellDTOList, result);

    }

    // 성공 - 빈 리스트 반환
    @Test
    void readSellByJournalId_성공2() {
        // given
        List<SellDTO> expectedSellDTOList = new ArrayList<>();

        // stub
        given(sellDAO.readSellByJournalId(anyInt())).willReturn(expectedSellDTOList);

        // when
        List<SellDTO> result = sellService.readSellByJournalId(1);

        // then
        assertEquals(expectedSellDTOList, result);
    }

    // Jpa 에러
    @Test
    void readSellByJournalId_실패1() {
        // given
        List<SellDTO> expectedSellDTOList = Arrays.asList(
                SellDTO.builder()
                        .sellId(1)
                        .sellDate(LocalDateTime.now())
                        .sellPrice(500)
                        .sellQuantity(10)
                        .journalId(1)
                        .build(),
                SellDTO.builder()
                        .sellId(2)
                        .sellDate(LocalDateTime.now())
                        .sellPrice(800)
                        .sellQuantity(20)
                        .journalId(1)
                        .build()
        );

        // stub
        String expectedExceptionMessage = "데이터베이스 연결 오류";
        given(sellDAO.readSellByJournalId(anyInt())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });

        // when & then

        assertThrows(DataAccessException.class, () -> {
            sellService.readSellByJournalId(1);
        });
    }





    // 성공
    @Test
    void deleteSellBySellId_성공1() {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(100)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("N");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        // stub
        given(journalsRepoForSell.findById(anyInt())).willReturn(Optional.ofNullable(journal));
        given(sellRepository.save(any(Sell.class))).willReturn(sell);
        given(sellRepository.findAllByJournal(journal)).willReturn(sellList);


        // when
        String result = sellService.deleteSellBySellId(sellRequestDTO);

        assertEquals(result , "삭제 성공");
    }

    // 해당하는 매매일지가 없는 경우
    @Test
    void deleteSellBySellId_실패1() {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(100)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("N");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        // stub
        given(journalsRepoForSell.findById(anyInt())).willThrow(new NoSuchElementException());


        // when & then

        assertThrows(SellException.class, () -> {
            sellService.deleteSellBySellId(sellRequestDTO);
        });
    }

    // Jpa 에러
    @Test
    void deleteSellBySellId_실패2() {

        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(200)
                .journalId(1)
                .build();

        Journals journal = Journals.builder()
                .journalId(1)
                .journalDate(LocalDateTime.now())
                .stockName("삼성")
                .totalQuantity(100)
                .avgBuyPrice( 500)
                .totalBuyQuantity(100)
                .strategyId(1)
                .fee(0.1)
                .status("Y")
                .users(Users.builder()
                        .email("test@test.com")
                        .password("123").build())
                .build();

        Sell sell = sellRequestDTO.convertToEntity();
        sell.setStatus("N");
        sell.setJournal(journal);

        List<Sell> sellList = new ArrayList<>();
        sellList.add(sell);

        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(journalsRepoForSell.findById(anyInt())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });


        // when & then

        assertThrows(DataAccessException.class, () -> {
            sellService.deleteSellBySellId(sellRequestDTO);
        });
    }

}