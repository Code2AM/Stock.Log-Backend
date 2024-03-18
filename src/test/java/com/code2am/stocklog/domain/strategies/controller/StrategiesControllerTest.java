package com.code2am.stocklog.domain.strategies.controller;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.code2am.stocklog.domain.strategies.service.StrategiesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StrategiesController.class)
@AutoConfigureMockMvc(addFilters = false)
class StrategiesControllerTest {

    @MockBean
    private StrategiesService strategiesService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /* createStrategy */


    // 성공
    @Test
    void createStrategy_성공() throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName("매매전략1")
                .build();

        String message = "정상적으로 등록되었습니다.";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.createStrategy(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // strategiesDTO의 제목이 없는 경우
    @Test
    void createStrategy_제목없음()throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName(null)
                .build();

        String message = "매매일지의 이름은 비어있을 수 없습니다.";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.createStrategy(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 사용자 인증이 되지 않는 경우




    /* readStrategiesByUserId */


    // 성공
    @Test
    void readStrategiesByUserId_성공() throws Exception{
        //given
        List<StrategiesDTO> expectedStrategiesDTOS = Arrays.asList(
                new StrategiesDTO(1, "strategy1"),
                new StrategiesDTO(2, "strategy2")
        );

        String requestBody = objectMapper.writeValueAsString(expectedStrategiesDTOS);

        // stub
        given(strategiesService.readStrategiesByUserId()).willReturn(expectedStrategiesDTOS);

        // when & then
        mockMvc.perform(post("/strategies/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
    }

    // 반환받은 리스트가 null인 경우
    @Test
    void readStrategiesByUserId_성공_빈리스트() throws Exception{
        //given
        List<StrategiesDTO> expectedStrategiesDTOS = new ArrayList<> ();

        String requestBody = objectMapper.writeValueAsString(expectedStrategiesDTOS);

        // stub
        given(strategiesService.readStrategiesByUserId()).willReturn(expectedStrategiesDTOS);

        // when & then
        mockMvc.perform(post("/strategies/findAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
    }

    // 사용자 인증이 되지 않는 경우



    /* deleteStrategyByStrategyIdAndUserId */


    // 성공
    void deleteStrategyByStrategyIdAndUserId_성공() throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName("매매전략1")
                .build();

        String message = "성공적으로 삭제되었습니다.";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.deleteStrategyByStrategyIdAndUserId(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // strategyId가 없는 경우
    void deleteStrategyByStrategyIdAndUserId_아이디없음() throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName(null)
                .build();

        String message = "매매일지의 이름은 비어있을 수 없습니다.";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.deleteStrategyByStrategyIdAndUserId(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 사용자 인증이 되지 않는 경우



    /* updateStrategy */


    // 성공
    void updateStrategy_성공() throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName("매매전략1")
                .build();

        String message = "수정 성공!";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.updateStrategy(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // strategyId가 없는 경우

    // strategiesDTO의 제목이 없는 경우
    void updateStrategy_제목없음() throws Exception{
        // given
        StrategiesDTO strategiesDTO = StrategiesDTO.builder()
                .strategyName(null)
                .build();

        String message = "매매일지의 이름은 비어있을 수 없습니다.";
        String requestBody = objectMapper.writeValueAsString(strategiesDTO);

        // stub
        given(strategiesService.updateStrategy(strategiesDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/strategies/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }


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