package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelsService {

    @Autowired
    private LabelsRepository labelsRepository;

    @Autowired
    private LabelsDAO labelsDAO;

    public List<LabelsDTO> readLabelsByUserId(Integer userId){
        return labelsDAO.readLabelsByUserId(userId);
    }

    public String createLabelsByUserId(LabelsDTO labelsDTO, Integer userId){

        if(labelsDTO.getLabelsTitle().isEmpty()){
            return "제목을 입력해주세요";
        }

        Labels newLabel = new Labels();

        newLabel.setLabelsTitle(labelsDTO.getLabelsTitle());
        newLabel.setUserId(userId);
        newLabel.setLabelsStatus("Y");

        labelsRepository.save(newLabel);

        return "성공";
    }

    @Transactional
    public String updateLabelByLabelsId(LabelsDTO labelsDTO) {

        // PK를 사용하여 해당 레코드를 검색
        LabelsDTO updateLabel = labelsDAO.readLabelsByLabelsId(labelsDTO.getLabelsId());

        if(updateLabel == null){
            return "해당 라벨이 존재하지 않습니다.";
        }

        // 업데이트
        updateLabel.setLabelsTitle(labelsDTO.getLabelsTitle());

        // 빈 Entity 객체 생성
        Labels newLabel = new Labels();

        // 수정
        newLabel.setLabelsTitle(updateLabel.getLabelsTitle());

        // 수정된 엔티티를 저장하여 업데이트 수행
        labelsRepository.save(newLabel);

        return "성공";
    }
}
