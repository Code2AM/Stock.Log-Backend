package com.code2am.stocklog.domain.labels.controller;


import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.service.LabelsService;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        given(labelsService.readLabelsByUserId()).willReturn(expectedLabelsDTOList);
        String requestBody = objectMapper.writeValueAsString(expectedLabelsDTOList);

        // when & then
        mockMvc.perform(post("/labels/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(requestBody));
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

    // 사용자 인증이 되지 않는 경우




    /* createLabelsByUserId */


    // 성공
    @Test
    void createLabelsByUserId_성공_() throws Exception {
        // given
        LabelsDTO newLabelDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
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
                .andExpect(content().string("라벨 제목, 텅 빈 공간은 NO!"));
    }

    // 사용자 인증이 되지 않는 경우


    /* updateLabelsByLabelsId */


    // 성공
    @Test
    void updateLabelsByLabelsId_라벨제목_비어있음_() throws Exception {
        // given
        LabelsDTO updatedLabelDTO = LabelsDTO.builder()
                .labelsTitle("updatedLabel")
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
                .andExpect(content().string("라벨 제목, 텅 빈 공간은 NO!"));
    }

    // 사용자 인증이 되지 않는 경우



    /* deleteLabelsByLabelsId */


    // 성공
    @Test
    void deleteLabelsByLabelsId_성공_() throws Exception {
        // given
        LabelsDTO deleteLabelDTO = LabelsDTO.builder()
                .labelsTitle("deleteLabelDTO")
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
}