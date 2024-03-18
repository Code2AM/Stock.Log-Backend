package com.code2am.stocklog.domain.journals.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.journals.dao.JournalsDAO;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.vo.JournalsVO;
import com.code2am.stocklog.domain.journals.repository.JournalsRepository;
import com.code2am.stocklog.domain.journals.service.JournalsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(JournalsController.class)
@AutoConfigureMockMvc(addFilters = false)
class JournalsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JournalsService journalsService;

    @MockBean
    private JournalsRepository journalsRepository;

    @MockBean
    private JournalsDAO journalsDAO;

    @MockBean
    private AuthUtil authUtil;

    @Test
    void 매매일지_등록_성공() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setStockName("테스트");
        journalsDTO.setBuyPrice(1000);
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setFee(0.3);

        Integer userId = 1;

        String success = "등록 성공";

        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        given(authUtil.getUserId()).willReturn(userId);
        given(journalsService.createJournalsByUserId(journalsDTO, userId)).willReturn(success);

        mockMvc.perform(post("/journals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 사용자_정보를_가져오지_못하는_경우() throws Exception {
        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setStockName("테스트");
        journalsDTO.setBuyPrice(1000);
        journalsDTO.setBuyQuantity(100);
        journalsDTO.setFee(0.3);


        String error = "사용자 정보를 가져올 수 없습니다.";

        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        given(journalsService.createJournalsByUserId(journalsDTO, null)).willReturn(error);

        mockMvc.perform(post("/journals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(error));
    }

    @Test
    void 매매일지_정보가_전달되지_못한_경우() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setFee(0.1);
        journalsDTO.setBuyQuantity(1);
        journalsDTO.setBuyPrice(100);
        String error = "종목이 선택되지 않았습니다.";

        Integer userId = 1;
        given(authUtil.getUserId()).willReturn(userId);

        String requestBody = objectMapper.writeValueAsString(journalsDTO);

        mockMvc.perform(post("/journals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 매매일지_조회_성공() throws Exception {

        Integer userId = 10;

        JournalsDTO data = new JournalsDTO();
        data.setJournalId(1);

        List<JournalsDTO> list = new ArrayList<>();
        list.add(data);

        String response = objectMapper.writeValueAsString(list);
        given(authUtil.getUserId()).willReturn(userId);
        given(journalsService.readJournalsByUserId(userId)).willReturn(list);

        mockMvc.perform(get("/journals")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 매매일지_조회는_성공했으나_데이터가_없는_경우() throws Exception {

        Integer userId = 10;

        JournalsDTO data = new JournalsDTO();
        data.setJournalId(1);

        List<JournalsDTO> list = new ArrayList<>();

        String response = objectMapper.writeValueAsString(list);
        given(authUtil.getUserId()).willReturn(userId);
        given(journalsService.readJournalsByUserId(userId)).willReturn(list);

        mockMvc.perform(get("/journals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void 사용자_정보가_없어_조회_실패() throws Exception {

        Integer userId = null;

        JournalsDTO data = new JournalsDTO();
        data.setJournalId(1);

        String error = "사용자 정보를 획득할 수 없습니다.";

        List<JournalsDTO> list = new ArrayList<>();

        String response = objectMapper.writeValueAsString(list);
        given(authUtil.getUserId()).willReturn(userId);

        mockMvc.perform(get("/journals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(error));
    }

    @Test
    void 매매일지_삭제_성공() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setJournalId(100);

        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        String success = "삭제 성공";
        given(journalsService.deleteJournalsByJournalsId(journalsDTO.getJournalId())).willReturn(success);

        mockMvc.perform(post("/journals/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 매매일지_삭제_요청값이_제대로_전달되지_않은_경우() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();

        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        String error = "요청값이 없습니다.";

        mockMvc.perform(post("/journals/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 삭제할_매매일지가_없는_경우() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setJournalId(100);

        String error = "조회된 결과가 없습니다.";
        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        given(journalsService.deleteJournalsByJournalsId(journalsDTO.getJournalId())).willReturn(error);

        mockMvc.perform(post("/journals/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string(error));
    }

    @Test
    void 매매일지_상태_변경_성공() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setJournalId(100);

        String success = "상태 변경 성공";
        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        given(journalsService.updateJournalsStatusByJournalId(journalsDTO.getJournalId())).willReturn(success);

        mockMvc.perform(post("/journals/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(success));
    }

    @Test
    void 매매일지_요청값이_전달되지_않은_경우() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();

        String error = "매매일지가 입력되지 않았습니다.";
        String requestBody = objectMapper.writeValueAsString(journalsDTO);

        mockMvc.perform(post("/journals/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(error));
    }

    @Test
    void 변경하고자_하는_매매일지가_없는_경우() throws Exception {

        JournalsDTO journalsDTO = new JournalsDTO();
        journalsDTO.setJournalId(100);

        String error = "없는 매매일지입니다.";
        String requestBody = objectMapper.writeValueAsString(journalsDTO);
        given(journalsService.updateJournalsStatusByJournalId(journalsDTO.getJournalId())).willReturn(error);

        mockMvc.perform(post("/journals/change")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string(error));
    }
}