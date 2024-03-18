package com.code2am.stocklog.domain.strategies.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.strategies.dao.StrategiesDAO;
import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.code2am.stocklog.domain.strategies.models.entity.Strategies;
import com.code2am.stocklog.domain.strategies.models.entity.UsersAndStrategies;
import com.code2am.stocklog.domain.strategies.repository.StrategiesRepository;
import com.code2am.stocklog.domain.strategies.repository.UsersAndStrategiesRepository;
import com.code2am.stocklog.domain.users.models.entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class StrategiesService {

    @Autowired
    private StrategiesRepository strategiesRepository;

    @Autowired
    private StrategiesDAO strategiesDAO;

    @Autowired
    private UsersAndStrategiesRepository usersAndStrategiesRepository;

    @Autowired
    private AuthUtil authUtil;

    /** 매매전략 생성 */
    public String createStrategy(StrategiesDTO strategy) {

        // 복합키, 중간 테이블을 이용해 직접적인 관계를 만들지 않도록 한다.
        UsersAndStrategies usersAndStrategies = new UsersAndStrategies();

        Integer userId = authUtil.getUserId();

       // 동일한 매매전략이 있는지 확인
        StrategiesDTO findResult = readStrategyByStrategyName(strategy.getStrategyName());


        // 해당 strategy 가 있는 경우
        if(!Objects.isNull(findResult)){
            usersAndStrategies.setUserId(userId);
            usersAndStrategies.setStrategyId(findResult.getStrategyId());
            usersAndStrategies.setStrategyName(findResult.getStrategyName());

            usersAndStrategiesRepository.save(usersAndStrategies);

        }
        // 해당 strategy가 없는 경우
        else {
            strategy.setStrategyStatus("Y");
            Strategies newStrategy = strategiesRepository.save(strategy.convertToEntity());

            usersAndStrategies.setUserId(userId);
            usersAndStrategies.setStrategyId(newStrategy.getStrategyId());
            usersAndStrategies.setStrategyName(newStrategy.getStrategyName());

            usersAndStrategiesRepository.save(usersAndStrategies);
        }

        return "정상적으로 등록되었습니다.";
    }


    /** 매매전략을 조회하는 메소드 */
    public List<StrategiesDTO> readStrategies() {
        return strategiesDAO.readStrategies();
    }


    /** 사용자의 매매전략을 수정하는 메소드 */
    public String updateStrategy(StrategiesDTO strategy) {

        UsersAndStrategies usersAndStrategies = new UsersAndStrategies();

        Integer userId = authUtil.getUserId();

        // 일단 수정된 이름이 실존하는지 확인
        StrategiesDTO findResult = readStrategyByStrategyName(strategy.getStrategyName());

        // 현 매매전략의 Linking pk를 알아냄
        UsersAndStrategies foundUserAndStrategy = usersAndStrategiesRepository.findByUserIdAndStrategyId(userId,strategy.getStrategyId());

        // 있는 경우
        if(!Objects.isNull(findResult)){
            foundUserAndStrategy.setStrategyId(findResult.getStrategyId());
            foundUserAndStrategy.setStrategyName(findResult.getStrategyName());

            usersAndStrategiesRepository.save(foundUserAndStrategy);

        }
        //  없는 경우
        else {
            strategy.setStrategyStatus("Y");
            strategy.setStrategyId(null);
            Strategies newStrategy = strategiesRepository.save(strategy.convertToEntity());


            foundUserAndStrategy.setStrategyId(newStrategy.getStrategyId());
            foundUserAndStrategy.setStrategyName(newStrategy.getStrategyName());

            usersAndStrategiesRepository.save(usersAndStrategies);
        }

        return "수정 성공!";
    }

    /** 매매전략을 작성한 사용자의 id 값과 자체 id 값을 이용해 매매전략을 삭제하는 메소드 */
    public String deleteStrategyByStrategyIdAndUserId(StrategiesDTO strategy) {

        Integer userId = authUtil.getUserId();

        Integer strategyId = strategy.getStrategyId();

        System.out.println("userID : "+userId+" strategyId : "+strategyId);

        usersAndStrategiesRepository.deleteByUserIdAndStrategyId(userId,strategyId);

        return "성공적으로 삭제되었습니다.";

    }










    /**
     * 매매전략을 해당 id 값을 이용해 조회하는 메소드
     * */
    public StrategiesDTO readStrategyByStrategyId(Integer strategyId){
        return strategiesDAO.readStrategyByStrategyId(strategyId);
    }

    /**
     * 매매전략을 해당 이름으로 조회하는 메소드
     * */
    public StrategiesDTO readStrategyByStrategyName(String strategyName){
        return strategiesDAO.readStrategyByStrategyName(strategyName);
    }

    /**
     * 매매전략을 사용자의 id 값을 이용해 리스트로 반환하도록 조회하는 메소드
     * */
    public List<StrategiesDTO> readStrategiesByUserId() {

        Integer userId = authUtil.getUserId();

        List<UsersAndStrategies> strategies = usersAndStrategiesRepository.findAllByUserId(userId);

        return strategies.stream()
                .map(UsersAndStrategies::convertToDTO)
                .toList();
    }

    /** 매매전략을 삭제하는 메소드 */
    public void deleteStrategyByStrategyId(StrategiesDTO strategy) {
        // 실제로는 삭제가 아닌 상태를 변경하는 로직

        StrategiesDTO find = readStrategyByStrategyId(strategy.getStrategyId());

        // 상태가 "Y"인 것 중 해당되는 것이 없다면 로직을 정지
        if(Objects.isNull(find)){
            return;
        }

        Strategies deleteStrategy = new Strategies();
        deleteStrategy.setStrategyId(strategy.getStrategyId());
        deleteStrategy.setStrategyName(strategy.getStrategyName());
        deleteStrategy.setStrategyStatus("N");

        strategiesRepository.save(deleteStrategy);
    }




}
