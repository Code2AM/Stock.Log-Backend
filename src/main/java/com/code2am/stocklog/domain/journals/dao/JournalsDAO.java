package com.code2am.stocklog.domain.journals.dao;

import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JournalsDAO {
    List<JournalsDTO> readJournalsByUserId(Integer userId);
}
