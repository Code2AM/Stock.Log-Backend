package com.code2am.stocklog.domain.journals.repository;

import com.code2am.stocklog.domain.journals.models.entity.Journals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalsRepository extends JpaRepository<Journals, Integer> {
    void findByUserId(Integer userId);
}
