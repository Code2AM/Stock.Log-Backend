package com.code2am.stocklog.domain.stocks.controller;


import com.code2am.stocklog.domain.stocks.models.dto.StocksDTO;
import com.code2am.stocklog.domain.stocks.service.StocksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/stocks")
public class StocksController {

    @Autowired
    private StocksService stockService;

    @PostMapping("/save")
    public String fetchAndSaveStocks() {
        try {
            stockService.fetchAndSaveStockInfo();
            return "성공";
        } catch (Exception e) {
            return "실패: " + e.getMessage();
        }
    }


}
