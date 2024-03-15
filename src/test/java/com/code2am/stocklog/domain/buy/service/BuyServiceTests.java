package com.code2am.stocklog.domain.buy.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.code2am.stocklog.domain.buy.dao.BuyDAO;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.repository.BuyRepository;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.journals.repository.JournalsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BuyServiceTests {

    @InjectMocks
    BuyService buyService;

    @Mock
    BuyDAO buyDAO;

    @Mock
    BuyRepository buyRepository;

    @Mock
    JournalsRepository journalsRepository;




    /* createBuy */

    // journal Id가 존재하지 않는 경우

    // buyPrice가 0 이하인 경우

    // 매수량이 0 이하인 경우

    // buyRepository에 정상적으로 등록이 되지 않은 경우


    /* deleteBuyByBuyId */
    @Test
    void deleteBuyByBuyId() {
    }

    // 성공한 경우

    // 삭제 실패한 경우

    // 매수기록이 없는 경우


    /* readBuyByJournalId */
    @Test
    void readBuyByJournalId() {
    }

    // 성공한 경우

    // 매수기록이 없는 경우


    /* readBuyQuantityByJournalId */
    @Test
    void readBuyQuantityByJournalId() {
    }

    // 성공한 경우

    // 총 매수물량 조회에 실패한 경우
    // 매수물량 결과가 -1이 나온 경우, 0이 나온 경우
}