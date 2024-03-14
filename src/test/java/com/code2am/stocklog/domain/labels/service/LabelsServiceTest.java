package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

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
    @Test
    void createLabelsByUserId() {
    }


    /* updateLabelByLabelsId */
    @Test
    void updateLabelByLabelsId() {
    }


    /* deleteLabelsByLabelsId */
    @Test
    void deleteLabelsByLabelsId() {
    }
}