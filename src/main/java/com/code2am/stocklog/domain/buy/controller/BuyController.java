package com.code2am.stocklog.domain.buy.controller;

import com.code2am.stocklog.domain.auth.common.util.SecurityUtil;
import com.code2am.stocklog.domain.buy.models.dto.BuyDTO;
import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.service.BuyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/buy")
@Tag(name = "매수 관리 API", description = "매수 기록을 관리하는 API")
public class BuyController {

    @Autowired
    private BuyService buyService;

    @Autowired
    private SecurityUtil securityUtil;

    @Operation(
            summary = "매수 등록",
            description = "매수를 등록합니다.",
            tags = {"POST"}
    )
    @PostMapping
    public ResponseEntity createBuy(@RequestBody Buy buy){

        if(Objects.isNull(buy)){
            return ResponseEntity.badRequest().body("입력값이 존재하지않습니다.");
        }

        String result = buyService.createBuy(buy);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매수 조회",
            description = "유저 정보를 통해 매수기록을 조회합니다.",
            tags = {"GET"}
    )
    @GetMapping
    public List<BuyDTO> readBuyByJournalId(@RequestBody Buy buy){

        Integer journalId = buy.getJournals().getJournalId();

        List<BuyDTO> result = buyService.readBuyByJournalId(journalId);

        if(result.isEmpty()){
            return null;
        }

        return result;
    }

    @Operation(
            summary = "매수 삭제",
            description = "매수 기록을 삭제합니다.",
            tags = {"DELETE"}
    )
    @DeleteMapping
    public ResponseEntity deleteBuyByBuyId(@RequestBody BuyDTO buyDTO){

        Integer buyId = buyDTO.getBuyId();

        if(Objects.isNull(buyId)){
            return ResponseEntity.badRequest().body("존재하지 않는 매수기록입니다.");
        }

        String result = buyService.deleteBuyByBuyId(buyId);

        return ResponseEntity.ok(result);
    }
}