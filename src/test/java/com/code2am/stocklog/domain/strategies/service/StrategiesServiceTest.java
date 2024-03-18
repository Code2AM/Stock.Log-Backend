package com.code2am.stocklog.domain.strategies.service;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

    // 데이터베이스 저장 실패, jpa 관련 문제


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

    // 데이터베이스 저장 실패, jpa 관련 문제


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
        verify(usersAndStrategiesRepository).findByUserIdAndStrategyId(1,1);
        assertEquals(result, message);
    }

    // 성공 - 수정한 이름이 없는 경우
    @Test
    void updateStrategy_성공_중복_이름_없음() {
    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다

    // 데이터베이스 저장 실패, jpa 관련 문제


    /* deleteStrategyByStrategyIdAndUserId */


    // 성공
    @Test
    void deleteStrategyByStrategyIdAndUserId_성공() {
    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다

    // 데이터베이스 저장 실패, jpa 관련 문제


    @Test
    void readStrategyByStrategyId() {
    }

    @Test
    void readStrategyByStrategyName() {
    }

    @Test
    void readStrategiesByUserId() {
    }

    @Test
    void deleteStrategyByStrategyId() {
    }


}