package com.code2am.stocklog.domain.strategies.repository;

import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import com.code2am.stocklog.domain.strategies.models.entity.UsersAndStrategies;
import com.code2am.stocklog.domain.users.models.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersAndStrategiesRepository extends JpaRepository<UsersAndStrategies, Integer> {

//    void findAllByUserId(Integer userId);

    List<UsersAndStrategies> findAllByUserId(Integer userId);

//    void findAllByUserIdAndStrategyId();
//    void findByUserIdAndStrategyId(Integer userId, Integer strategyId);

    Integer deleteByUserIdAndStrategyId(Integer userId, Integer strategyId);


    UsersAndStrategies findByUserIdAndStrategyId(Integer userId, Integer strategyId);
}
