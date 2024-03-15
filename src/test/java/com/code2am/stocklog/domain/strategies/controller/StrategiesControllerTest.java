package com.code2am.stocklog.domain.strategies.controller;

import com.code2am.stocklog.domain.strategies.service.StrategiesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StrategiesController.class)
@AutoConfigureMockMvc(addFilters = false)
class StrategiesControllerTest {

    @MockBean
    private StrategiesService strategiesService;

    @MockBean
    private MockMvc mockMvc;

    @MockBean
    private ObjectMapper objectMapper;

    /* createStrategy */
    @Test
    void createStrategy() {
    }

    // 성공

    // strategiesDTO의 제목이 없는 경우

    // 사용자 인증이 되지 않는 경우




    /* readStrategiesByUserId */
    @Test
    void readStrategiesByUserId() {
    }

    // 성공

    // 반환받은 리스트가 null인 경우

    // 사용자 인증이 되지 않는 경우



    /* deleteStrategyByStrategyIdAndUserId */
    @Test
    void deleteStrategyByStrategyIdAndUserId() {
    }

    // 성공

    // strategyId가 없는 경우

    // 사용자 인증이 되지 않는 경우



    /* updateStrategy */
    @Test
    void updateStrategy() {
    }

    // 성공

    // strategyId가 없는 경우

    // strategiesDTO의 제목이 없는 경우

    // 사용자 인증이 되지 않는 경우






    @Test
    void readStrategyByStrategyId() {
    }

    @Test
    void readStrategies() {
    }

    @Test
    void deleteStrategyByStrategyId() {
    }
}