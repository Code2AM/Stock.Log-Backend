package com.code2am.stocklog.domain.labels.controller;


import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.service.LabelsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LabelsController.class)
@AutoConfigureMockMvc(addFilters = false)
class LabelsControllerTest {

    @MockBean
    private LabelsService labelsService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthUtil authUtil;

    private LabelsDTO labelsDTO;

    @BeforeEach
    void setup() {
        labelsDTO = new LabelsDTO();
        labelsDTO.setLabelsId(1);
        labelsDTO.setLabelsTitle("라벨");
        labelsDTO.setUserId(1);
        labelsDTO.setLabelsStatus("Y");
    }

    /* readLabelsByUserId */


    // 성공
    @Test
    void readLabelsByUserId_성공() throws Exception {
        // given
        List<LabelsDTO> expectedLabelsDTOList = Arrays.asList(
                new LabelsDTO(1, "label1"),
                new LabelsDTO(2, "label2")
        );

        // stub
        String requestBody = objectMapper.writeValueAsString(expectedLabelsDTOList);

        given(labelsService.readLabelsByUserId()).willReturn(expectedLabelsDTOList);

        // when & then
        mockMvc.perform(post("/labels/get")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    // 반환받은 리스트가 null인 경우
    @Test
    void readLabelsByUserId_성공_빈리스트() throws Exception {
        // given
        List<LabelsDTO> expectedLabelsDTOList = new ArrayList<>();

        // stub
        given(labelsService.readLabelsByUserId()).willReturn(expectedLabelsDTOList);
        String requestBody = objectMapper.writeValueAsString(expectedLabelsDTOList);

        // when & then
        mockMvc.perform(post("/labels/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
    }



    /* createLabelsByUserId */

    // 성공
    @Test
    void createLabelsByUserId_성공_() throws Exception {
        // given
        LabelsDTO newLabelDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .userId(1)
                .build();

        String message = "라벨추가 성공";

        // stub
        given(labelsService.createLabelsByUserId(newLabelDTO)).willReturn(message);
        String requestBody = objectMapper.writeValueAsString(newLabelDTO);

        // when & then
        mockMvc.perform(post("/labels/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // LabelDTO의 제목이 없는 경우
    @Test
    void createLabelsByUserId_라벨제목_비어있음() throws Exception {
        // given
        LabelsDTO newLabelDTO = LabelsDTO.builder()
                .labelsTitle(null)
                .build();

        String message = "라벨추가 성공";

        // stub
        given(labelsService.createLabelsByUserId(newLabelDTO)).willReturn(message);
        String requestBody = objectMapper.writeValueAsString(newLabelDTO);

        // when & then
        mockMvc.perform(post("/labels/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("최소 한 글자 이상 입력해주세요"));
    }

    // 사용자 인증이 되지 않는 경우
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void createLabelsByUserId_실패_1() throws Exception {
        // given
        LabelsDTO labels = LabelsDTO.builder()
                .labelsTitle("라벨")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        mockMvc.perform(post("/labels/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labels)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedExceptionMessage));
    }

    /* updateLabelsByLabelsId */


    // 성공
    @Test
    void updateLabelsByLabelsId_라벨제목_비어있음_() throws Exception {
        // given
        LabelsDTO updatedLabelDTO = LabelsDTO.builder()
                .labelsTitle("updatedLabel")
                .userId(1)
                .build();

        String message = "수정 성공";

        // stub
        given(labelsService.updateLabelByLabelsId(updatedLabelDTO)).willReturn(message);
        String requestBody = objectMapper.writeValueAsString(updatedLabelDTO);

        // when & then
        mockMvc.perform(post("/labels/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // LabelDTO의 제목이 없는 경우
    @Test
    void updateLabelsByLabelsId_성공_() throws Exception {
        // given
        LabelsDTO updatedLabelDTO = LabelsDTO.builder()
                .labelsTitle(null)
                .build();

        String message = "수정 성공";

        // stub
        given(labelsService.updateLabelByLabelsId(updatedLabelDTO)).willReturn(message);
        String requestBody = objectMapper.writeValueAsString(updatedLabelDTO);

        // when & then
        mockMvc.perform(post("/labels/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("최소 한 글자 이상 입력해주세요"));
    }

    // 사용자 인증이 되지 않는 경우
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void updateLabelsByUserId_실패_1() throws Exception {
        // given
        LabelsDTO labels = LabelsDTO.builder()
                .labelsTitle("라벨")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        mockMvc.perform(post("/labels/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labels)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedExceptionMessage));
    }


    /* deleteLabelsByLabelsId */


    // 성공
    @Test
    void deleteLabelsByLabelsId_성공_() throws Exception {
        // given
        LabelsDTO deleteLabelDTO = LabelsDTO.builder()
                .labelsTitle("deleteLabelDTO")
                .userId(1)
                .build();

        String message = "삭제 성공";

        // stub
        given(labelsService.deleteLabelsByLabelsId(deleteLabelDTO)).willReturn(message);
        String requestBody = objectMapper.writeValueAsString(deleteLabelDTO);

        // when & then
        mockMvc.perform(post("/labels/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // 사용자 인증이 되지 않는 경우
    @Test
    @DisplayName("인증된 사용자가 없는 경우")
    public void deleteLabelsByUserId_실패_1() throws Exception {
        // given
        LabelsDTO labels = LabelsDTO.builder()
                .labelsTitle("라벨")
                .build();

        String expectedExceptionMessage = "인증된 사용자가 없습니다";

        // stub
        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));

        // when & then
        mockMvc.perform(post("/labels/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(labels)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedExceptionMessage));
    }
}