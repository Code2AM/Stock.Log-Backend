package com.code2am.stocklog.domain.sell.controller;

import com.code2am.stocklog.domain.sell.models.dto.InputDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellDTO;
import com.code2am.stocklog.domain.sell.models.dto.SellRequestDTO;
import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.service.SellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/sell")
@Tag(name = "매도 API", description = "매도 기록을 관리하는 API")
@RequiredArgsConstructor
public class SellController {


    private final SellService sellService;

    @Operation(
            summary = "매도 등록",
            description = "매도 기록을 등록합니다."
    )
    @Parameter(name = "sellRequestDTO", description = "등록할 매도 기록 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매도 기록 등록 성공"),
            @ApiResponse(responseCode = "400", description = "매도 기록 오입력")
    })
    @PostMapping
    public ResponseEntity createSell(@Valid @RequestBody SellRequestDTO sellRequestDTO){

        String result = sellService.createSell(sellRequestDTO);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매도 조회",
            description = "매매일지 정보를 통해 매도 기록을 조회합니다."
    )
    @Parameter(name = "inputDTO", description = "매도 기록에 연관된 매매일지의 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매도 기록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "매매일지 정보 전달 안됨")
    })
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
            summary = "매도 삭제",
            description = "매도 기록을 삭제합니다."
    )
    @Parameter(name = "sellRequestDTO", description = "삭제하고자 하는 매도기록 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매도 기록 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "매도 기록 정보 오입력"),
            @ApiResponse(responseCode = "404", description = "삭제하고자 하는 매도 기록 없음")
    })
    @PostMapping("/delete")
    public ResponseEntity deleteSellBySellId(@RequestBody SellRequestDTO sellRequestDTO){

        String result = sellService.deleteSellBySellId(sellRequestDTO);

        return ResponseEntity.ok(result);
    }


}
