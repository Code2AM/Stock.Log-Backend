package com.code2am.stocklog.domain.sell.controller;

import com.code2am.stocklog.domain.sell.models.entity.Sell;
import com.code2am.stocklog.domain.sell.service.SellService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity createSell(@RequestBody Sell sell){

        if(Objects.isNull(sell)){
            return ResponseEntity.badRequest().body("잘못된 입력입니다.");
        }

        String result = sellService.createSell(sell);

        ResponseEntity.ok(result);
    }


}
