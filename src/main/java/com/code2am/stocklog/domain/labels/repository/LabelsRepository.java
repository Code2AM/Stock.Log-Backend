package com.code2am.stocklog.domain.labels.repository;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelsRepository extends JpaRepository<Labels, Integer> {
}
