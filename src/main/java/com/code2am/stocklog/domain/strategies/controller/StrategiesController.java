package com.code2am.stocklog.domain.strategies.controller;

import com.code2am.stocklog.domain.strategies.models.dto.StrategiesDTO;
import com.code2am.stocklog.domain.strategies.models.dto.UsersIdDTO;
import com.code2am.stocklog.domain.strategies.models.entity.UsersAndStrategies;
import com.code2am.stocklog.domain.strategies.service.StrategiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/strategies")
@Tag(name = "매매전략 관리 API", description = "매매 전략을 관리하는 API")
public class StrategiesController {

    @Autowired
    private StrategiesService strategiesService;


    /* 매매전략 등록 */
    /**
     * 매매전략을 등록하는 메소드
     * @param strategy
     * */
    @Operation(
            summary = "매매전략 등록",
            description = "매매전략을 등록합니다."
    )
    @Parameter(name = "strategy", description = "사용자가 입력한 매매전략")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createStrategy(@Valid @RequestBody StrategiesDTO strategy){

        String result = strategiesService.createStrategy(strategy);

        return ResponseEntity.ok("정상적으로 등록되었습니다.");
    }


    /* 매매전략 조회(all) */
    @Operation(
            summary = "사용자용, 매매전략 조회",
            description = "사용자가 자신의 매매전략을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @GetMapping("/findAll")
    public ResponseEntity<List<StrategiesDTO>> readStrategiesByUserId(){

        return ResponseEntity.ok(strategiesService.readStrategiesByUserId());
    }


    /* 매매전략 삭제 */
    @Operation(
            summary = "사용자용, 매매전략 삭제",
            description = "사용자가 자신의 매매전략을 삭제합니다. 실제 동작에 있어서는 관계만을 끊는 것으로 DB의 데이터를 손상시키지 않습니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다."),
            @ApiResponse(responseCode = "404", description = "삭제하고자 하는 매매전략 없음")
    })
    @Parameter(name = "strategy", description = "삭제하고자 하는 매매전략의 정보")
    @PostMapping("/delete")
    public ResponseEntity <String> deleteStrategyByStrategyIdAndUserId(@Valid @RequestBody StrategiesDTO strategy){

        String result = strategiesService.deleteStrategyByStrategyIdAndUserId(strategy);

        return ResponseEntity.ok(result);
    }


    /* 매매전략 수정 */
    @Operation(
            summary = "사용자용, 매매전략 수정",
            description = "사용자가 자신의 매매전략을 수정합니다"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다."),
            @ApiResponse(responseCode = "404", description = "수정하고자 하는 매매전략 없음")
    })
    @Parameter(name = "strategy", description = "수정하고자 하는 매매전략 정보")
    @PostMapping("/update")
    public ResponseEntity<String> updateStrategy(@RequestBody StrategiesDTO strategy){ // 매개변수는 추후 수정

        return ResponseEntity.ok(strategiesService.updateStrategy(strategy));
    }


    /* 매매전략 조회 (이름) */
    @Operation(
            summary = "매매전략 이름 조회",
            description = "특정한 매매전략을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @Parameter(name = "strategiesDTO", description = "조회하고자 하는 매매전략의 이름 정보")
    @PostMapping("/read")
    public StrategiesDTO readStrategyByStrategyId(@RequestBody StrategiesDTO strategiesDTO){

        System.out.println("내가 보낸 값" + strategiesDTO);

        if(Objects.isNull(strategiesDTO)){
            System.out.println("입력값이 없습니다.");
            return null;
        }
        Integer strategyId = strategiesDTO.getStrategyId();

        return strategiesService.readStrategyByStrategyId(strategyId);
    }








    /**
     * 매매전략을 조회하는 메소드
     * @return 모든 매매전략
     * */
    @Operation(
            summary = "매매전략을 조회",
            description = "모든 매매전략을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @PostMapping("/getAllAdmin")
    public List<StrategiesDTO> readStrategies(){
        // 관리자용 조회(관리자는 조회와 삭제만 가능하도록)
        return strategiesService.readStrategies();
    }


    @Operation(
            summary = "관리자용, 매매전략을 삭제",
            description = "관리자가 매매전략을 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매전략 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @Parameter(name = "strategy", description = "관리자가 삭제할 매매전략")
    @PostMapping("/deleteAdmin")
    public void deleteStrategyByStrategyId(@RequestBody StrategiesDTO strategy){
        // 관리자용 삭제
        strategiesService.deleteStrategyByStrategyId(strategy);
    }
}
