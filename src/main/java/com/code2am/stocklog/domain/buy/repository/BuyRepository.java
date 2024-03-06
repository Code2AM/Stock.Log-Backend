package com.code2am.stocklog.domain.buy.repository;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyRepository extends JpaRepository<Buy, Integer> {
}
