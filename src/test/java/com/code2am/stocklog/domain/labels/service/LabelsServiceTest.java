package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LabelsServiceTest {

    @InjectMocks
    private LabelsService labelsService;

    @Mock
    private LabelsRepository labelsRepository;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private LabelsDAO labelsDAO;

    @Mock
    private UsersRepository usersRepository;


    /* readLabelsByUserId */

    // 성공
    @Test
    @DisplayName("정상 (사용자 ID에 해당하는 레이블이 있는 경우)")
    public void readLabelsByUserId_성공_1() {
        // given
        List<LabelsDTO> expectedLabelsDTOList = Arrays.asList(
                new LabelsDTO(1, "label1"),
                new LabelsDTO(2, "label2")
        );

        // stub

            // 사용자 아이디를 1로 설정
        given(authUtil.getUserId()).willReturn(1);
        given(labelsDAO.readLabelsByUserId(1)).willReturn(expectedLabelsDTOList);

        // when
        List<LabelsDTO> actualLabelsDTOList = labelsService.readLabelsByUserId();

        // then
        verify(labelsDAO).readLabelsByUserId(1); // 메올바르게 호출 되었는지 확인
        assertEquals(expectedLabelsDTOList, actualLabelsDTOList);
    }

    // 조회했는데 값이 없는 경우
    @Test
    @DisplayName("정상 (사용자 ID에 해당하는 레이블이 없는 경우)")
    public void readLabelsByUserId_성공_2() {
        // given
        List<LabelsDTO> expectedLabelsDTOList = new ArrayList<>();

        // stub
        given(authUtil.getUserId()).willReturn(1);
        given(labelsDAO.readLabelsByUserId(1)).willReturn(expectedLabelsDTOList);

        // when
        List<LabelsDTO> actualLabelsDTOList = labelsService.readLabelsByUserId();

        // then
        verify(labelsDAO).readLabelsByUserId(1);
        assertEquals(expectedLabelsDTOList, actualLabelsDTOList);
    }

    // 예외 (데이터베이스 연결 오류)
    @Test
    @DisplayName("예외 (데이터베이스 연결 오류)")
    public void readLabelsByUserId_실패_1() {
        // given
        String expectedExceptionMessage = "데이터베이스 연결 오류";

        // stub
        given(authUtil.getUserId()).willReturn(1);
        given(labelsDAO.readLabelsByUserId(1)).willThrow(new DataAccessException(expectedExceptionMessage) {
        });

        // when
        try {
            labelsService.readLabelsByUserId();
            fail("예외가 발생하지 않았습니다.");
        } catch (DataAccessException e) {
            // then
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    // 예외 (존재하지 않는 사용자 ID)
    @Test
    @DisplayName("예외 (존재하지 않는 사용자 ID)")
    public void readLabelsByUserId_실패_2() {
        // given
        String expectedExceptionMessage = "존재하지 않는 사용자 ID";

        // stub
        given(authUtil.getUserId()).willReturn(100); // 존재하지 않는 사용자 ID
        given(labelsDAO.readLabelsByUserId(100)).willThrow(new NoSuchElementException(expectedExceptionMessage));

        // when
        try {
            labelsService.readLabelsByUserId();
            fail("예외가 발생하지 않았습니다.");
        } catch (NoSuchElementException e) {
            // then
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }


    /* createLabelsByUserId */

    // 성공
    @Test
    void createLabelsByUserId_성공(){
        // given

        // 전달받은 dto
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .build();

        Labels expectedLabel = Labels.builder()
                .userId(1)
                .labelsTitle("Label1")
                .labelsStatus("Y")
                .build();

        // stub
        given(authUtil.getUserId()).willReturn(1);
        given(labelsRepository.save(any(Labels.class))).willReturn(expectedLabel);

        //when
        String result = labelsService.createLabelsByUserId(labelsDTO);

        // then
        assertEquals("라벨추가 성공",result);
    }

    // authUtil 관련 예외 상황들
    @Test
    void createLabelsByUserId_UserIdIsNull() {
        // given
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Test Label")
                .build();

        when(authUtil.getUserId()).thenReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> labelsService.createLabelsByUserId(labelsDTO),
                "사용자 ID가 없습니다.");
    }


    // LabelsDTO 가 null 인 경우
    @Test
    void createLabelsByUserId_DTO값이_NULL인경우() {
        // given
        LabelsDTO labelsDTO = null;

        // when & then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> labelsService.createLabelsByUserId(labelsDTO)
        );

        // 예외 메시지 확인
        assertEquals("LabelsDTO의 값이 없습니다.", exception.getMessage());
    }


    /* updateLabelByLabelsId */

    // 성공
    @Test
    void updateLabelByLabelsId_성공(){
        // given

            // 전달받은 dto
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .build();

        // stub
        given(authUtil.getUserId()).willReturn(1);

        // when
        String result = labelsService.updateLabelByLabelsId(labelsDTO);

        // then
        assertEquals("수정 성공",result);
    }

    // authUtil 예외
    @Test
    void updateLabelByLabelsId_UserIdIsNull() {
        // given
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Test Label")
                .build();

        when(authUtil.getUserId()).thenReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> labelsService.updateLabelByLabelsId(labelsDTO),
                "사용자 ID가 없습니다.");
    }

    // repo에서 jpa관련 예외
    @Test
    void updateLabelByLabelsId_무결성_제약조건_위반() {
        // given
        LabelsDTO labelsDTO = new LabelsDTO();
        labelsDTO.setLabelsId(1);
        labelsDTO.setUserId(1); // 사용자 ID 설정

        // save() 메소드가 호출될 때 DataIntegrityViolationException이 발생하도록 설정
        doThrow(DataIntegrityViolationException.class).when(labelsRepository).save(any());

        // when & then
        assertEquals("데이터베이스 무결성 제약 조건 위반으로 인한 수정 실패", labelsService.updateLabelByLabelsId(labelsDTO));
    }

    // convertEntity 예외



    /* deleteLabelsByLabelsId */


    // 성공
    @Test
    void deleteLabelsByLabelsId_성공(){
        // given

        // 전달받은 dto
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .build();

        // stub
        given(authUtil.getUserId()).willReturn(1);

        // when
        String result = labelsService.deleteLabelsByLabelsId(labelsDTO);

        // then
        assertEquals("삭제 성공",result);
    }

    // authUtil 예외
    @Test
    void deleteLabelsByLabelsId_UserIdIsNull() {
        // given
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Test Label")
                .build();

        when(authUtil.getUserId()).thenReturn(null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> labelsService.deleteLabelsByLabelsId(labelsDTO),
                "사용자 ID가 없습니다.");
    }


    // convertEntity 예외

    // repo에서 jpa관련 예외
    @Test
    void deleteLabelsByLabelsId_무결성_제약조건_위반() {
        // given
        LabelsDTO labelsDTO = new LabelsDTO();
        labelsDTO.setLabelsId(1);
        labelsDTO.setUserId(1); // 사용자 ID 설정

        // labelsRepository.save() 메서드가 호출될 때 DataIntegrityViolationException 발생하도록 설정
        when(labelsRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("데이터베이스 무결성 제약 조건 위반"));

        given(authUtil.getUserId()).willReturn(1);

        // when & then
        assertEquals("데이터베이스 무결성 제약 조건 위반으로 인한 삭제 실패", labelsService.deleteLabelsByLabelsId(labelsDTO));
    }

}