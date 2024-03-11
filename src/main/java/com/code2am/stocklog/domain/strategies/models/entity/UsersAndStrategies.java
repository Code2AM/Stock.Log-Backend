package com.code2am.stocklog.domain.strategies.models.entity;


import com.code2am.stocklog.domain.users.models.entity.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@IdClass(UsersAndStrategiesId.class) // 복합키 사용을 위한 식별자 클래스
@Table(name = "TBL_USERS_AND_STRATEGIES")
@Data
@Builder
public class UsersAndStrategies {

//    @Id
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "USER_ID")
//    private Users users;
//
//    @Id
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "STRATEGY_ID")
//    private Strategies strategies;


    @Id
    @Column(name = "USER_AND_STRATEGY_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer UserAndStrategyId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "STRATEGY_ID")
    private Integer strategyId;

    @Column(name = "STRATEGY_NAME")
    private String strategyName;


    public UsersAndStrategies() {

    }
}
