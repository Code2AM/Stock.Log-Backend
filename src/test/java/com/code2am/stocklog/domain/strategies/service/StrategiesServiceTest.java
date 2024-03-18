package com.code2am.stocklog.domain.strategies.service;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.strategies.dao.StrategiesDAO;
import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.code2am.stocklog.domain.strategies.models.entity.UsersAndStrategies;
import com.code2am.stocklog.domain.strategies.repository.StrategiesRepository;
import com.code2am.stocklog.domain.strategies.repository.UsersAndStrategiesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;


@ExtendWith(MockitoExtension.class)
class StrategiesServiceTest {

    @InjectMocks
    private StrategiesService strategiesService;

    @Mock
    private StrategiesRepository strategiesRepository;

    @Mock
    private StrategiesDAO strategiesDAO;

    @Mock
    private UsersAndStrategiesRepository usersAndStrategiesRepository;

    @Mock
    private AuthUtil authUtil;


    /* createStrategy */


    // 성공 - 동일한 매매전략이 있는 경우
    @Test
    @DisplayName("정상 - 동일한 매매전략이 있는 경우")
    void createStrategy_성공_이름_있음() {
        // given
        StrategiesDTO newStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        StrategiesDTO strategyResult = StrategiesDTO.builder()
                .strategyId(1)
                .strategyName("strategy")
                .build();

        String message = "정상적으로 등록되었습니다.";

        // stub
        given(strategiesService.readStrategyByStrategyName(newStrategy.getStrategyName())).willReturn(strategyResult);

        // when
        String result = strategiesService.createStrategy(newStrategy);

        // then
        verify(strategiesDAO).readStrategyByStrategyName(newStrategy.getStrategyName());
        assertEquals(result, message);

    }

    // 성공 - 동일한 매매전략이 없는 경우
    @Test
    void createStrategy_성공_이름_없음() {
        // given
        StrategiesDTO newStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();


        String message = "정상적으로 등록되었습니다.";

        // stub
        given(strategiesService.readStrategyByStrategyName(newStrategy.getStrategyName())).willReturn(new StrategiesDTO());

        // when
        String result = strategiesService.createStrategy(newStrategy);

        // then
        verify(strategiesDAO).readStrategyByStrategyName(newStrategy.getStrategyName());
        assertEquals(result, message);

    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void createStrategy_실패_1() {
        // given
        StrategiesDTO newStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        try {
            strategiesService.createStrategy(newStrategy);
        } catch (AuthUtilException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }


    // 데이터베이스 저장 실패, jpa 관련 문제
    @Test
    @DisplayName("데이터베이스 저장 실패, jpa 관련 문제")
    public void createStrategy_실패_2() {
        // given
        StrategiesDTO newStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(authUtil.getUserId()).willReturn(1);

        given(strategiesService.readStrategyByStrategyName(newStrategy.getStrategyName())).willReturn(new StrategiesDTO());
        given(usersAndStrategiesRepository.save(any())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });

        // when
        try {
            strategiesService.createStrategy(newStrategy);
        } catch (DataAccessException e) {
            // then
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }



    /* readStrategies */


    // 성공 - 리스트 반환
    @Test
    void readStrategies_성공() {
        //given
        List<StrategiesDTO> expectedStrategiesDTOS = Arrays.asList(
                new StrategiesDTO(1, "strategy1"),
                new StrategiesDTO(2, "strategy2")
        );

        // stub
        given(strategiesDAO.readStrategies()).willReturn(expectedStrategiesDTOS);

        // when
        List<StrategiesDTO> strategiesDTOS = strategiesService.readStrategies();

        // then
        verify(strategiesDAO).readStrategies();
        assertEquals(expectedStrategiesDTOS, strategiesDTOS);
    }

    // 성공 - 빈 리스트 반환
    @Test
    void readStrategies_빈리스트() {
        //given
        List<StrategiesDTO> expectedStrategiesDTOS = new ArrayList<>();

        // stub
        given(strategiesDAO.readStrategies()).willReturn(expectedStrategiesDTOS);

        // when
        List<StrategiesDTO> strategiesDTOS = strategiesService.readStrategies();

        // then
        verify(strategiesDAO).readStrategies();
        assertEquals(expectedStrategiesDTOS, strategiesDTOS);
    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void readStrategiesByUserId_실패_1() {
        //given

        String expectedExceptionMessage = "인증된 사용자가 없습니다";


        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        AuthUtilException exception = assertThrows(AuthUtilException.class, () -> {
            strategiesService.readStrategiesByUserId();
        });

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }




    /* updateStrategy */


    // 성공 - 수정한 이름이 실존해는 경우
    @Test
    void updateStrategy_성공_중복_이름_있음() {
        // given
        StrategiesDTO updateStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        StrategiesDTO strategyResult = StrategiesDTO.builder()
                .strategyId(1)
                .strategyName("strategy")
                .strategyStatus("Y")
                .build();

        UsersAndStrategies foundResult = UsersAndStrategies.builder()
                .userAndStrategyId(1)
                .userId(1)
                .strategyId(1)
                .strategyName("strategy")
                .build();

        String message = "수정 성공!";

        // stub
        given(authUtil.getUserId()).willReturn(1);
            // 중복확인
        given(strategiesService.readStrategyByStrategyName(any())).willReturn(strategyResult);

        given(usersAndStrategiesRepository.findByUserIdAndStrategyId(any(), any())).willReturn(foundResult);

        // when
        String result = strategiesService.updateStrategy(updateStrategy);

        // then
        assertEquals(result, message);
    }

    // 성공 - 수정한 이름이 없는 경우
    @Test
    void updateStrategy_성공_중복_이름_없음() {
        // given
        StrategiesDTO updateStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        UsersAndStrategies foundResult = UsersAndStrategies.builder()
                .userAndStrategyId(1)
                .userId(1)
                .strategyId(1)
                .strategyName("strategy")
                .build();

        String message = "수정 성공!";

        // stub
        given(authUtil.getUserId()).willReturn(1);
        // 중복확인
        given(strategiesService.readStrategyByStrategyName(any())).willReturn(new StrategiesDTO());

        given(usersAndStrategiesRepository.findByUserIdAndStrategyId(any(), any())).willReturn(foundResult);

        // when
        String result = strategiesService.updateStrategy(updateStrategy);

        // then
        assertEquals(result, message);
    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void updateStrategy_실패_1() {
        //given
        StrategiesDTO updateStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        try {
            strategiesService.updateStrategy(updateStrategy);
        } catch (AuthUtilException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    // 데이터베이스 저장 실패, jpa 관련 문제
    @Test
    @DisplayName("데이터베이스 저장 실패, jpa 관련 문제")
    public void updateStrategy_실패_2() {
        // given
        StrategiesDTO updateStrategy = StrategiesDTO.builder()
                .strategyName("strategy")
                .build();

        UsersAndStrategies foundResult = UsersAndStrategies.builder()
                .userAndStrategyId(1)
                .userId(1)
                .strategyId(1)
                .strategyName("strategy")
                .build();

        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(authUtil.getUserId()).willReturn(1);

        given(strategiesService.readStrategyByStrategyName(updateStrategy.getStrategyName())).willReturn(new StrategiesDTO());

        given(usersAndStrategiesRepository.findByUserIdAndStrategyId(any(), any())).willReturn(foundResult);
        given(usersAndStrategiesRepository.save(any())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });


        // when
        try {
            strategiesService.updateStrategy(updateStrategy);
        } catch (DataAccessException e) {
            // then
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    /* deleteStrategyByStrategyIdAndUserId */


    // 성공
    @Test
    void deleteStrategyByStrategyIdAndUserId_성공() {
        // given
        StrategiesDTO deleteStrategy = StrategiesDTO.builder()
                .strategyId(1)
                .strategyName("strategy")
                .strategyStatus("Y")
                .build();

        String message = "성공적으로 삭제되었습니다.";

        // stub
        given(authUtil.getUserId()).willReturn(1);

        // when
        String result = strategiesService.deleteStrategyByStrategyIdAndUserId(deleteStrategy);

        // then
        assertEquals(message, result);


    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void deleteStrategyByStrategyIdAndUserId_실패_1() {
        //given
        StrategiesDTO deleteStrategy = StrategiesDTO.builder()
                .strategyId(1)
                .strategyName("strategy")
                .strategyStatus("Y")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        try {
            strategiesService.deleteStrategyByStrategyIdAndUserId(deleteStrategy);
        } catch (AuthUtilException e) {
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }


    // 데이터베이스 저장 실패, jpa 관련 문제
    @Test
    @DisplayName("데이터베이스 저장 실패, jpa 관련 문제")
    public void deleteStrategyByStrategyIdAndUserId_실패_2() {
        // given
        StrategiesDTO deleteStrategy = StrategiesDTO.builder()
                .strategyId(1)
                .strategyName("strategy")
                .build();


        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(authUtil.getUserId()).willReturn(1);

        given(usersAndStrategiesRepository.deleteByUserIdAndStrategyId(anyInt(), anyInt())).willThrow(new DataAccessException(expectedExceptionMessage) {
        });

        // when
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            strategiesService.deleteStrategyByStrategyIdAndUserId(deleteStrategy);
        });

        assertEquals(expectedExceptionMessage, exception.getMessage());
    }



    @Test
    void readStrategyByStrategyId() {
    }

    @Test
    void readStrategyByStrategyName() {
    }


    @Test
    void deleteStrategyByStrategyId() {
    }


}