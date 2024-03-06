package com.code2am.stocklog.domain.comments.dao;

import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentsDAO {
    List<CommentsVO> readCommentsByJournalId(Integer journalId);
}
