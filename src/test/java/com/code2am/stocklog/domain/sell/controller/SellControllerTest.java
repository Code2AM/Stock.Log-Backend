package com.code2am.stocklog.domain.sell.controller;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.handler.exceptions.ValidationException;
import com.code2am.stocklog.domain.sell.models.dto.InputDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellRequestDTO;
import com.code2am.stocklog.domain.sell.service.SellService;
import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.DisplayName;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(SellController.class)
@AutoConfigureMockMvc(addFilters = false)
class SellControllerTest {

    @MockBean
    private SellService sellService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    // 성공
    @Test
    void createSell_성공() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(10)
                .journalId(1)
                .build();
        String message = "등록 성공";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // stub
        given(sellService.createSell(sellRequestDTO)).willReturn(message);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // 매도 날짜 미입력
    @Test
    void createSell_날짜_미입력() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellPrice(1000)
                .sellQuantity(10)
                .journalId(1)
                .build();
        String message = "매도 날짜는 비어있을 수 없습니다";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);


        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));

    }

    // 매도 가격 미입력
    @Test
    void createSell_가격_미입력() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellQuantity(10)
                .journalId(1)
                .build();

        String message = "매도 가격은 비어있을 수 없습니다";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 매도 가격 0 이하
    @Test
    void createSell_가격_0_이하() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(-1)
                .sellQuantity(10)
                .journalId(1)
                .build();

        String message = "매도가는 0 이하일 수 없습니다.";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 매도 물량 미입력
    @Test
    void createSell_물량_미입력() throws Exception {

        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .journalId(1)
                .build();

        String message = "매도 물량은 비어있을 수 없습니다";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 매도 물량 0 이하
    @Test
    void createSell_물량_0_이하() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(-1)
                .journalId(1)
                .build();

        String message = "매도량이 0 이하일 수 없습니다.";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }

    // 싹 다 미입력
    @Test
    void createSell_미입력() throws Exception {
        // given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder().build();

        String message = "매도 날짜는 비어있을 수 없습니다";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // when & then
        mockMvc.perform(post("/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }



    // 성공 - 리스트 반환
    @Test
    @DisplayName("정상 - 리스트 반환")
    void readSellByJournalId_성공1() throws Exception {
        //given
        InputDTO inputDTO = new InputDTO();
        inputDTO.setJournalId(1);

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

        String inputData = objectMapper.writeValueAsString(inputDTO);
        String requestBody = objectMapper.writeValueAsString(expectedSellDTOList);


        // stub
        given(sellService.readSellByJournalId(1)).willReturn(expectedSellDTOList);

        // when & then
        mockMvc.perform(post("/sell/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputData))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
    }

    // 성공 - 빈 리스트 반환
    @Test
    @DisplayName("정상 - 빈 리스트 반환")
    void readSellByJournalId_성공2() throws Exception {
        //given
        InputDTO inputDTO = new InputDTO();
        inputDTO.setJournalId(1);

        List<SellDTO> expectedSellDTOList = new ArrayList<>();

        String inputData = objectMapper.writeValueAsString(inputDTO);
        String requestBody = objectMapper.writeValueAsString(expectedSellDTOList);


        // stub
        given(sellService.readSellByJournalId(1)).willReturn(expectedSellDTOList);

        // when & then
        mockMvc.perform(post("/sell/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputData))
                .andExpect(status().isOk());
    }

    // 매매일지 조회 안되는 경우
    @Test
    @DisplayName("매매일지 조회 안되는 경우")
    void readSellByJournalId_실패1() throws Exception{
        //given
        InputDTO inputDTO = new InputDTO();
        inputDTO.setJournalId(1);

        String message = "해당하는 매매일지가 없습니다.";

        String inputData = objectMapper.writeValueAsString(inputDTO);

        // stub
        given(sellService.readSellByJournalId(1)).willThrow(new ValidationException(message));

        // when & then
        mockMvc.perform(post("/sell/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputData))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));

    }



    // 성공
    @Test
    void deleteSellBySellId_성공()  throws Exception {
        //given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellId(1)
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(-1)
                .journalId(1)
                .build();

        String message = "삭제 성공";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // stub
        given(sellService.deleteSellBySellId(any(SellRequestDTO.class))).willReturn(message);

        // when & then
        mockMvc.perform(post("/sell/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));

    }

    // 매매일지 아이디가 없다
    @Test
    void deleteSellBySellId_매매일지_없음() throws Exception {
        //given
        SellRequestDTO sellRequestDTO = SellRequestDTO.builder()
                .sellId(1)
                .sellDate(LocalDateTime.now())
                .sellPrice(1000)
                .sellQuantity(-1)
                .journalId(1)
                .build();

        String message = "해당하는 매매일지 없음";

        String requestBody = objectMapper.writeValueAsString(sellRequestDTO);

        // stub
        given(sellService.deleteSellBySellId(any(SellRequestDTO.class))).willThrow(new ValidationException(message));

        // when & then
        mockMvc.perform(post("/sell/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(message));
    }


}