package com.code2am.stocklog.domain.sell.repository;

import com.code2am.stocklog.domain.sell.models.entity.Sell;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellRepository extends JpaRepository<Sell, Integer> {
}
