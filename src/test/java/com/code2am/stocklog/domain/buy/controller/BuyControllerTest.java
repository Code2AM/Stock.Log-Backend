package com.code2am.stocklog.domain.buy.controller;

import com.code2am.stocklog.domain.buy.dao.BuyDAO;
import com.code2am.stocklog.domain.buy.infra.JournalsRepoForBuy;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.dto.InputDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.service.BuyService;
import com.code2am.stocklog.domain.journals.dao.JournalsDAO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(BuyController.class)
@AutoConfigureMockMvc(addFilters = false)
class BuyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BuyService buyService;

    @MockBean
    private JournalsRepoForBuy journalsRepoForBuy;

    @MockBean
    private BuyDAO buyDAO;

    @Test
    void 매수_등록_성공() throws Exception {

        Journals journals = new Journals();
        journals.setJournalId(100);

        Buy buy = new Buy();
        buy.setBuyQuantity(100);
        buy.setBuyPrice(100);
        buy.setJournals(journals);
        buy.setBuyDate(LocalDateTime.now());

        List<BuyDTO> buylist = new ArrayList<>();
        buylist.add(buy.convertToDTO());

        String success = "등록 성공";

        given(buyDAO.readBuyByJournalId(buy.getJournals().getJournalId())).willReturn(buylist);
        given(journalsRepoForBuy.findById(buy.getJournals().getJournalId())).willReturn(Optional.of(journals));
        given(buyService.createBuy(buy)).willReturn(success);

        String requestBody = objectMapper.writeValueAsString(buy);
        mockMvc.perform(post("/buy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 존재하지_않는_매매일지의_경우() throws Exception {

        Journals journals = new Journals();
        journals.setJournalId(-1);

        Buy buy = new Buy();
        buy.setBuyQuantity(100);
        buy.setBuyPrice(100);
        buy.setJournals(journals);
        buy.setBuyDate(LocalDateTime.now());

        String error = "존재하지 않는 매매일지입니다.";
        String requestBody = objectMapper.writeValueAsString(buy);

        given(buyService.createBuy(buy)).willReturn(error);

        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 매수가가_0_이하인_경우() throws Exception {

        Journals journals = new Journals();
        journals.setJournalId(1);

        Buy buy = new Buy();
        buy.setBuyQuantity(100);
        buy.setBuyPrice(0);
        buy.setJournals(journals);
        buy.setBuyDate(LocalDateTime.now());

        String error = "매수가는 0 이하일 수 없습니다.";
        String requestBody = objectMapper.writeValueAsString(buy);

        given(buyService.createBuy(buy)).willReturn(error);

        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 매수량이_0_이하인_경우() throws Exception {

        Journals journals = new Journals();
        journals.setJournalId(1);

        Buy buy = new Buy();
        buy.setBuyQuantity(0);
        buy.setBuyPrice(100);
        buy.setJournals(journals);
        buy.setBuyDate(LocalDateTime.now());

        String error = "매수량이 0 이하일 수 없습니다.";
        String requestBody = objectMapper.writeValueAsString(buy);

        given(buyService.createBuy(buy)).willReturn(error);

        mockMvc.perform(post("/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 매수_조회_성공() throws Exception {
        InputDTO inputDTO = new InputDTO();
        inputDTO.setJournalId(100);

        BuyDTO buy = new BuyDTO();
        buy.setBuyId(1);
        buy.setStatus("Y");
        buy.setBuyQuantity(100);
        buy.setBuyPrice(100);
        buy.setBuyDate(LocalDateTime.now());

        List<BuyDTO> list = new ArrayList<>();
        list.add(buy);

        String response = objectMapper.writeValueAsString(list);
        String requestBody = objectMapper.writeValueAsString(inputDTO);

        given(buyService.readBuyByJournalId(inputDTO.getJournalId())).willReturn(list);

        mockMvc.perform(post("/buy/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 매수_조회_성공했으나_반환값이_없는_경우() throws Exception {
        // Given
        InputDTO inputDTO = new InputDTO();
        inputDTO.setJournalId(100);

        List<BuyDTO> list = new ArrayList<>();

        String requestBody = objectMapper.writeValueAsString(inputDTO);
        String responseBody = objectMapper.writeValueAsString(list);

        given(buyService.readBuyByJournalId(inputDTO.getJournalId())).willReturn(list);

        // When and Then
        mockMvc.perform(post("/buy/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    @Test
    void deleteBuyByBuyId() {
    }
}