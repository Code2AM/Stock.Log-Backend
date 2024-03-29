package com.code2am.stocklog.domain.buy.controller;

import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.dto.InputDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.service.BuyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/buy")
@Tag(name = "매수 관리 API", description = "매수 기록을 관리하는 API")
@RequiredArgsConstructor
public class BuyController {

    private final BuyService buyService;

    @Operation(
            summary = "매수 등록",
            description = "매수를 등록합니다."
    )
    @Parameter(name = "buy", description = "사용자가 매수한 기록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값이 존재하지 않습니다."),
            @ApiResponse(responseCode = "400", description = "존재하지 않는 매매일지입니다."),
            @ApiResponse(responseCode = "400", description = "매수가는 0 이하일 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "매수량이 0 이하일 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "매수 기록이 없습니다.")
    })
    @PostMapping
    public ResponseEntity<String> createBuy(@RequestBody Buy buy){

        String result = buyService.createBuy(buy);

        return switch (result) {
            case "매매일지 정보가 없습니다.", "존재하지 않는 매매일지입니다.", "매수가는 0 이하일 수 없습니다.", "매수량이 0 이하일 수 없습니다." -> ResponseEntity.badRequest().body(result);
            case "매수 기록이 없습니다.", "매매일지가 없습니다." -> ResponseEntity.status(404).body(result);
            default -> ResponseEntity.ok(result);
        };        
    }

    @Operation(
            summary = "매수 조회",
            description = "유저 정보를 통해 매수기록을 조회합니다."
    )
    @PostMapping("/list")
    public List<BuyDTO> readBuyByJournalId(@RequestBody InputDTO inputDTO){
        System.out.println(inputDTO);

        Integer journalId = inputDTO.getJournalId();

        List<BuyDTO> result = buyService.readBuyByJournalId(journalId);

        if(result.isEmpty()){
            return null;
        }
        System.out.println(result);
        return result;
    }

    @Operation(
            summary = "매수 삭제",
            description = "매수 기록을 삭제합니다."
    )
    @PostMapping("/delete")
    public ResponseEntity<String> deleteBuyByBuyId(@RequestBody BuyDTO buyDTO){

        Integer buyId = buyDTO.getBuyId();

        if(Objects.isNull(buyId)){
            return ResponseEntity.badRequest().body("존재하지 않는 매수기록입니다.");
        }

        String result = buyService.deleteBuyByBuyId(buyId);

        if(result.equals("404")){
            return ResponseEntity.status(404).body("해당하는 매수 기록이 존재하지 않습니다.");
        } else if (result.equals("평균값 등록 실패")) {
            return ResponseEntity.status(404).body("해당하는 매매일지가 없습니다.");
        }else {
            return ResponseEntity.ok(result);
        }
    }
}