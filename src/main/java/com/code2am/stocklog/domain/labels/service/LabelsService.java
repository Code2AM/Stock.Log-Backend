package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<LabelsDTO> readLabelsByUserId(Integer userId){
        return labelsDAO.readLabelsByUserId(userId);
    }

    // 라벨을 추가하는 메소드
    public String createLabelsByUserId(LabelsDTO labelsDTO){

        if(labelsDTO.getLabelsTitle().isEmpty()){
            return "제목을 입력해주세요";
        }

        Labels newLabel = new Labels();

        newLabel.setLabelsTitle(labelsDTO.getLabelsTitle());
        newLabel.setUserId(authUtil.getUserId());
        newLabel.setLabelsStatus("Y");

        labelsRepository.save(newLabel);

        return "성공";
    }

    // 라벨을 수정하는 메소드
    @Transactional
    public String updateLabelByLabelsId(LabelsDTO labelsDTO) {
        labelsDTO.setUserId(authUtil.getUserId());
        labelsRepository.save(labelsDTO.convertToEntity());
        System.out.println(labelsDTO.convertToEntity());
        return "수정 성공";
    }

    // 라벨을 삭제(상태값 변경)하는 메소드
    @Transactional
    public String deleteLabelsByLabelsId(LabelsDTO labelsDTO){
        Labels labels = labelsDTO.convertToEntity();
        labels.setUserId(authUtil.getUserId());
        labels.setLabelsStatus("N");
        labelsRepository.save(labels);
        return "삭제 성공";
    }
}
