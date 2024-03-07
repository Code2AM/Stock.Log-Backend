package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.common.utils.CommonUtils;
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
    CommonUtils commonUtils;

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

    // 라벨을 수정하는 메소드
    @Transactional
    public String updateLabelByLabelsId(LabelsDTO labelsDTO) {

        labelsRepository.save(commonUtils.convertLabelsDtoToEntity(labelsDTO));
        return "성공";
    }

    // 라벨을 삭제(상태값 변경)하는 메소드
    @Transactional
    public String deleteLabelsByLabelsId(Integer labelsId){
        // 삭제할 라벨 가져오기
        LabelsDTO deleteLabels = labelsDAO.readLabelsByLabelsId(labelsId);
        System.out.println("1" + deleteLabels.getLabelsId());
        // 유효성 검사
        if(deleteLabels == null){
            return "해당 라벨이 존재하지 않습니다.";
        }

        // 엔티티 객체 생성
        Labels labels = new Labels();

        // 엔티티의 상태 변경
        labels.setLabelsId(deleteLabels.getLabelsId());
        labels.setLabelsTitle(deleteLabels.getLabelsTitle());
        labels.setLabelsStatus("N");


        System.out.println("entity: " + labels.getLabelsId());

        // 저장하여 업데이트 수행
        labelsRepository.save(labels);

        return "라벨이 삭제되었습니다.";
    }
}
