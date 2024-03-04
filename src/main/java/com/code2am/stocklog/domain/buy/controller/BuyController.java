package com.code2am.stocklog.domain.buy.controller;

import com.code2am.stocklog.domain.buy.models.entity.Buy;
import com.code2am.stocklog.domain.buy.service.BuyService;
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
@RequestMapping("/buy")
@Tag(name = "매수 관리 API", description = "매수 기록을 관리하는 API")
public class BuyController {

    @Autowired
    private BuyService buyService;

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

}
