package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelsService {

    @Autowired
    private LabelsRepository labelsRepository;

    @Autowired
    private LabelsDAO labelsDAO;

    @Autowired
    private AuthUtil authUtil;

    public List<LabelsDTO> readLabelsByUserId() {

        // authentication 객체에 있는 userId를 받아온다
        Integer userId = authUtil.getUserId();

        return labelsDAO.readLabelsByUserId(userId);
    }


    // 라벨을 추가하는 메소드
    public String createLabelsByUserId(LabelsDTO labelsDTO) {
        if (labelsDTO == null) {
            throw new IllegalArgumentException("LabelsDTO의 값이 없습니다.");
        }

        if (authUtil.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 없습니다.");
        }

        Labels newLabel = Labels.builder()
                .userId(authUtil.getUserId())
                .labelsTitle(labelsDTO.getLabelsTitle())
                .labelsStatus("Y")
                .build();

        labelsRepository.save(newLabel);

        return "라벨추가 성공";
    }

    // 라벨을 수정하는 메소드
    @Transactional
    public String updateLabelByLabelsId(LabelsDTO labelsDTO) {
        // userId가 null값인 경우
        if (authUtil.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 없습니다.");
        }

        try {
            labelsDTO.setUserId(authUtil.getUserId());
            labelsRepository.save(labelsDTO.convertToEntity());
            return "수정 성공";
        } catch (DataIntegrityViolationException e) {
            // 데이터베이스 무결성 예외 처리
            return "데이터베이스 무결성 제약 조건 위반으로 인한 수정 실패";
        }
    }

    // 라벨을 삭제(상태값 변경)하는 메소드
    @Transactional
    public String deleteLabelsByLabelsId(LabelsDTO labelsDTO) {
        // userId가 null값인 경우
        if (authUtil.getUserId() == null) {
            throw new IllegalArgumentException("사용자 ID가 없습니다.");
        }

        try {
            labelsDTO.setUserId(authUtil.getUserId());
            labelsDTO.setLabelsStatus("N");
            labelsRepository.save(labelsDTO.convertToEntity());
            return "삭제 성공";
        } catch (DataIntegrityViolationException e) {
            // 데이터베이스 무결성 예외 처리
            return "데이터베이스 무결성 제약 조건 위반으로 인한 삭제 실패";
        }
    }
}
