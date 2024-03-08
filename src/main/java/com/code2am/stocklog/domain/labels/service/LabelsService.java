package com.code2am.stocklog.domain.labels.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.labels.repository.LabelsRepository;

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
    public String updateLabelByLabelsId(Integer labelsId) {
        LabelsDTO updateLabels = labelsDAO.readLabelsByLabelsId(labelsId);
        if(updateLabels == null){
            return "해당 라벨이 존재하지 않습니다.";
        }

        Integer userId = authUtil.getUserId();

        System.out.println(labelsId);
        Labels labels = commonUtils.convertLabelsDtoToEntity(updateLabels);
        labels.setUserId(userId);

        labelsRepository.save(labels);
        return "수정";
    }

    // 라벨을 삭제(상태값 변경)하는 메소드
    public String deleteLabelsByLabelsId(Integer labelsId){
        // 삭제할 라벨 가져오기
        LabelsDTO deleteLabels = labelsDAO.readLabelsByLabelsId(labelsId);
        System.out.println("1" + deleteLabels.getLabelsId());
        // 유효성 검사
        if(deleteLabels == null){
            return "해당 라벨이 존재하지 않습니다.";
        }

        Integer userId = authUtil.getUserId();
        // 엔티티 객체 생성
        Labels labels = new Labels();

        // 엔티티의 상태 변경
        labels.setLabelsId(deleteLabels.getLabelsId());
        labels.setLabelsTitle(deleteLabels.getLabelsTitle());
        labels.setLabelsStatus("N");
        labels.setUserId(userId);


        System.out.println("entity: " + labels.getLabelsId());

        // 저장하여 업데이트 수행
        labelsRepository.save(labels);

        return "라벨이 삭제되었습니다.";
    }
}
