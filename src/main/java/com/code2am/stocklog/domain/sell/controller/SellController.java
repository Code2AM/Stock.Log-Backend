package com.code2am.stocklog.domain.sell.controller;

import com.code2am.stocklog.domain.sell.models.dto.InputDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellRequestDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.service.SellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sell")
@Tag(name = "매도 API", description = "매도 기록을 관리하는 API")
public class SellController {

    @Autowired
    private SellService sellService;

    @Operation(
            summary = "매도 등록",
            description = "매도 기록을 등록합니다.",
            tags = {"POST"}
    )
    @PostMapping
    public ResponseEntity createSell(@Valid @RequestBody SellRequestDTO sellRequestDTO){

        String result = sellService.createSell(sellRequestDTO);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매도 조회",
            description = "매매일지 정보를 통해 매도 기록을 조회합니다.",
            tags = {"POST"}
    )
    @PostMapping("/list")
    public List<SellDTO> readSellByJournalId(@RequestBody InputDTO inputDTO){

        Integer journalId = inputDTO.getJournalId();

        List<SellDTO> result = sellService.readSellByJournalId(journalId);

        if(result.isEmpty()){
            return null;
        }

        return result;
    }

    @Operation(
            summary = "매매삭제",
            description = "매매기록을 삭제합니다.",
            tags = {"POST"}
    )
    @PostMapping("/delete")
    public ResponseEntity deleteSellBySellId(@Valid @RequestBody SellRequestDTO sellRequestDTO){

        String result = sellService.deleteSellBySellId(sellRequestDTO);

        return ResponseEntity.ok(result);
    }


}
