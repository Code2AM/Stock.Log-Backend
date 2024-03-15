package com.code2am.stocklog.domain.strategies.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.strategies.dao.StrategiesDAO;
import com.code2am.stocklog.domain.strategies.repository.StrategiesRepository;
import com.code2am.stocklog.domain.strategies.repository.UsersAndStrategiesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @Test
    void createStrategy() {
    }

    // 성공 - 동일한 매매전략이 있는 경우

    // 성공 - 동일한 매매전략이 없는 경우

    //

    /* readStrategies */
    @Test
    void readStrategies() {
    }

    // 성공 - 리스트 반환

    // 성공 - 빈 리스트 반환

    //

    /* deleteStrategyByStrategyIdAndUserId */
    @Test
    void deleteStrategyByStrategyIdAndUserId() {
    }

    //

    /* updateStrategy */
    @Test
    void updateStrategy() {
    }




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