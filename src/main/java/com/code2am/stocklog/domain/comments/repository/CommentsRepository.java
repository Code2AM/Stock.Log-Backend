package com.code2am.stocklog.domain.comments.repository;

import com.code2am.stocklog.domain.comments.models.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Integer> {
}
