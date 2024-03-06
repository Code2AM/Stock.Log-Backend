package com.code2am.stocklog.domain.labels.controller;

import com.code2am.stocklog.domain.labels.service.LabelsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labels")
@Tag(name = "라벨 API", description = "라벨을 관리하는 API")
public class LabelsController {

//    @Autowired
//    LabelsService labelsService;
//
//    @Operation(
//            summary = "라벨 등록",
//            description = "라벨을 등록합니다",
//            tags = {"POST"}
//    )
//    @PostMapping
//    public ResponseEntity createLabelsByUserId(){
//
//    }
}
