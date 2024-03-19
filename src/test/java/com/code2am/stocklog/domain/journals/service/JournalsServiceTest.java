package com.code2am.stocklog.domain.journals.service;

import com.code2am.stocklog.domain.journals.dao.JournalsDAO;
import com.code2am.stocklog.domain.journals.infra.BuyRepo;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.vo.JournalsVO;
import com.code2am.stocklog.domain.journals.repository.JournalsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class JournalsServiceTest {

//    AuthServiceTest > login_성공() FAILED
//    com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException at AuthServiceTest.java:245
//
//    JournalsServiceTest > 매매일지 삭제 성공 FAILED
//    java.lang.NullPointerException at JournalsServiceTest.java:186
//
//    JournalsServiceTest > 매매일지 거래 상태 변경 성공 FAILED
//    java.lang.NullPointerException at JournalsServiceTest.java:236
//
//    JournalsServiceTest > 매매일지 등록 성공 FAILED
//    java.lang.NullPointerException at JournalsServiceTest.java:83
//
//    LabelsControllerTest > 인증된 사용자가 없는 경우 FAILED
//    java.lang.AssertionError at LabelsControllerTest.java:285
//
//    LabelsControllerTest > 인증된 사용자가 없는 경우 FAILED
//    java.lang.AssertionError at LabelsControllerTest.java:166
//
//    LabelsControllerTest > 인증된 사용자가 없는 경우 FAILED
//    java.lang.AssertionError at LabelsControllerTest.java:236
//
//    NotesControllerTest > 인증된 사용자가 없는 경우 FAILED
//    java.lang.AssertionError at NotesControllerTest.java:167

    @InjectMocks
    private JournalsService journalsService;

    @Mock
    private JournalsRepository journalsRepository;

    @Mock
    private JournalsDAO journalsDAO;

    @Mock
    private BuyRepo buyRepo;

    @Test
    @DisplayName("매매일지 조회 성공")
    void 매매일지_조회_성공() {
        Integer userId = 1;
        JournalsDTO journal1 = new JournalsDTO();
        journal1.setJournalId(1);

        JournalsDTO journal2 = new JournalsDTO();
        journal2.setJournalId(2);

        List<JournalsDTO> list = new ArrayList<>();
        list.add(journal1);
        list.add(journal2);

        given(journalsDAO.readJournalsByUserId(userId)).willReturn(list);

        List<JournalsDTO> result = journalsDAO.readJournalsByUserId(userId);

        Assertions.assertEquals(result, list);
    }

    @Test
    @DisplayName("매매일지 조회시 사용자 정보 없음")
    void 매매일지_조회시_필요한_사용자_정보가_없는_경우(){

        given(journalsDAO.readJournalsByUserId(null)).willReturn(null);

        List<JournalsDTO> result = journalsDAO.readJournalsByUserId(null);

        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("매매일지 등록 성공")
    void 매매일지_등록_성공(){

        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setBuyPrice(100);
        journalsDTO.setFee(0.5);
        journalsDTO.setStockName("test");
        journalsDTO.setStrategyId(1);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("등록 성공", result);

    }

    @Test
    @DisplayName("수수료가 허용 범위를 벗어난 경우")
    void 수수료가_범위를_벗어난_경우(){
        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setBuyPrice(100);
        journalsDTO.setFee(10);
        journalsDTO.setStockName("test");
        journalsDTO.setStrategyId(1);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("수수료는 0 ~ 100% 사이 값만 사용할 수 있습니다.", result);
    }

    @Test
    @DisplayName("종목을 지정하지 않는 경우")
    void 종목을_선태하지_않은_경우(){
        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setBuyPrice(100);
        journalsDTO.setFee(0.1);
        journalsDTO.setStockName("");
        journalsDTO.setStrategyId(1);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("종목을 선택하지 않았습니다.", result);
    }

    @Test
    @DisplayName("매수가가 0보다 낮은 경우")
    void 매수가가_0보다_낮은_경우(){

        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setBuyPrice(-100);
        journalsDTO.setFee(0.1);
        journalsDTO.setStockName("test");
        journalsDTO.setStrategyId(1);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("정상적인 매수가가 아닙니다.", result);
    }

    @Test
    @DisplayName("매수량이 0보다 적은 경우")
    void 매수량이_0보다_적은_경우(){
        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(-100);
        journalsDTO.setBuyPrice(100);
        journalsDTO.setFee(0.1);
        journalsDTO.setStockName("test");
        journalsDTO.setStrategyId(1);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("매수량이 0보다 적습니다.", result);
    }

    @Test
    @DisplayName("매매전략이 비정상일 경우")
    void 비정상적인_매매전략이_입력된_경우(){
        Integer userId = 1;

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setBuyPrice(100);
        journalsDTO.setFee(0.1);
        journalsDTO.setStockName("test");
        journalsDTO.setStrategyId(0);

        String result = journalsService.createJournalsByUserId(journalsDTO, userId);

        Assertions.assertEquals("불가능한 매매전략 값입니다.", result);
    }

    @Test
    @DisplayName("매매일지 삭제 성공")
    void 매매일지_삭제_성공(){
        Integer journalId = 1;

        JournalsVO data = new JournalsVO();
        data.setJournalId(1);
        data.setStatus("open");

        given(journalsDAO.readJournalsByJournalId(journalId)).willReturn(data);

        String result = journalsService.deleteJournalsByJournalsId(journalId);

        Assertions.assertEquals("삭제 성공", result);
    }

    @Test
    @DisplayName("삭제할 결과가 없는 경우")
    void 삭제할_조회_결과가_없는_경우(){
        Integer journalId = 1;

        String result = journalsService.deleteJournalsByJournalsId(journalId);

        Assertions.assertEquals("조회된 결과가 없습니다.", result);
    }

    @Test
    @DisplayName("삭제에 필요한 매매일지 정보가 없는 경우")
    void 삭제할_매매일지의_정보가_제대로_넘어오지_않은_경우(){

        String result = journalsService.deleteJournalsByJournalsId(null);

        Assertions.assertEquals("매매일지 정보가 정상적으로 넘어오지 않았습니다.", result);
    }

    @Test
    @DisplayName("이미 삭제된 매매일지를 다시 삭제하려는 경우")
    void 이미_삭제된_매매일지를_다시_삭제하려는_경우(){
        Integer journalId = 1;

        JournalsVO journalsVO = new JournalsVO();
        journalsVO.setJournalId(1);
        journalsVO.setStatus("N");

        given(journalsDAO.readJournalsByJournalId(journalId)).willReturn(journalsVO);

        String result = journalsService.deleteJournalsByJournalsId(journalId);

        Assertions.assertEquals("이미 삭제된 매매일지입니다.", result);
    }
    @Test
    @DisplayName("매매일지 거래 상태 변경 성공")
    void 매매일지_거래_상태_변경_성공(){
        Integer journalId = 100;

        JournalsVO journalsVO = new JournalsVO();
        journalsVO.setJournalId(100);
        journalsVO.setStatus("open");

        given(journalsDAO.readJournalsByJournalId(journalId)).willReturn(journalsVO);

        String result = journalsService.updateJournalsStatusByJournalId(journalId);

        Assertions.assertEquals("상태 변경 성공", result);
    }

    @Test
    @DisplayName("존재하지 않는 매매일지를 변경하는 경우")
    void 존재하지_않는_매매일지를_변경하려는_경우(){

        Integer journalId = 100;

        given(journalsDAO.readJournalsByJournalId(journalId)).willReturn(null);

        String result = journalsService.updateJournalsStatusByJournalId(journalId);

        Assertions.assertEquals("없는 매매일지입니다.", result);
    }

    @Test
    @DisplayName("이미 닫힌 매매일지를 변경하려는 경우")
    void 이미_닫힌_상태인_매매일지를_변경하려는_경우(){

        Integer journalId = 1000;

        JournalsVO journalsVO = new JournalsVO();
        journalsVO.setJournalId(1000);
        journalsVO.setStatus("close");

        given(journalsDAO.readJournalsByJournalId(journalId)).willReturn(journalsVO);

        String result = journalsService.updateJournalsStatusByJournalId(journalId);
        Assertions.assertEquals("이미 닫힌 매매일지입니다.", result);
    }
}