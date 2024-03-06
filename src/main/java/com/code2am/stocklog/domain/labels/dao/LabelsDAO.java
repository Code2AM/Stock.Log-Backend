package com.code2am.stocklog.domain.labels.dao;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.vo.LabelsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabelsDAO {
    List<LabelsDTO> readLabelsByUserId(Integer userId);
    LabelsDTO readLabelsByLabelsId(Integer labelsId);
}
