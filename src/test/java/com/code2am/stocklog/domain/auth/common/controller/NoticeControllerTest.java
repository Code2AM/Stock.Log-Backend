//package com.code2am.stocklog.domain.auth.common.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ohgiraffers.finalproject.notice.dto.NoticeDTO;
//import com.ohgiraffers.finalproject.notice.service.NoticeService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//// Spring MVC 테스트를 위한 확장을 사용합니다.
//@ExtendWith(SpringExtension.class)
//// NoticeController를 웹 MVC 테스트 대상으로 설정합니다.
//@WebMvcTest(NoticeController.class)
//class NoticeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // NoticeService를 Mock으로 설정합니다.
//    @MockBean
//    private NoticeService noticeService;
//
//    // "전체 공지 사항 조회"에 대한 테스트 메소드입니다.
//    @Test
//    void allNotice_ReturnsListOfNoticeDTO() throws Exception {
//        // 공지사항 목록을 생성합니다.
//        List<NoticeDTO> noticeList = new ArrayList<>();
//        NoticeDTO noticeDTO = new NoticeDTO();
//        // NoticeDTO를 생성하고 공지사항 목록에 추가합니다.
//        noticeList.add(noticeDTO);
//
//        // noticeService.findAllNotice()가 호출될 때 noticeList를 반환하도록 설정합니다.
//        when(noticeService.findAllNotice()).thenReturn(noticeList);
//
//        // "/api/notice/allNotice" 엔드포인트에 GET 요청을 보내고, 응답을 확인합니다.
//        mockMvc.perform(get("/api/notice/allNotice"))
//                .andExpect(status().isOk()) // HTTP 응답 상태 코드가 200인지 확인합니다.
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//
//    // "공지 사항 검색"에 대한 테스트 메소드입니다.
//    @Test
//    void searchNotice_ReturnsListOfNoticeDTO() throws Exception {
//        // 검색할 텍스트를 설정합니다.
//        String inputText = "exampleInputText";
//
//        // 공지사항 목록을 생성합니다.
//        List<NoticeDTO> noticeList = new ArrayList<>();
//        NoticeDTO noticeDTO = new NoticeDTO();
//        // NoticeDTO를 생성하고 공지사항 목록에 추가합니다.
//        noticeList.add(noticeDTO);
//
//        // noticeService.findInputNotice(inputText)가 호출될 때 noticeList를 반환하도록 설정합니다.
//        when(noticeService.findInputNotice(inputText)).thenReturn(noticeList);
//
//        // "/api/notice/searchNotice/{inputText}" 엔드포인트에 GET 요청을 보내고, 응답을 확인합니다.
//        mockMvc.perform(get("/api/notice/searchNotice/{inputText}", inputText))
//                .andExpect(status().isOk()) // HTTP 응답 상태 코드가 200인지 확인합니다.
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON)); // 응답의 컨텐츠 타입이 JSON 형식인지 확인합니다.
//    }
//
//    // 다른 메소드에 대한 테스트도 유사한 방식으로 작성합니다.
//
//}
