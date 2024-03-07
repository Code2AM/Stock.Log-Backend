package com.code2am.stocklog.domain.labels.controller;

import com.code2am.stocklog.domain.auth.common.util.SecurityUtil;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.vo.LabelsVO;
import com.code2am.stocklog.domain.labels.service.LabelsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labels")
@Tag(name = "라벨 API", description = "라벨을 관리하는 API")
public class LabelsController {

    @Autowired
    LabelsService labelsService;

    @Autowired
    private SecurityUtil securityUtil;

    @Operation(
            summary = "라벨 조회",
            description = "사용자의 라벨을 조회합니다",
            tags = {"GET"}
    )
    @GetMapping
    public ResponseEntity readLabelsByUserId(){
        Integer userId = securityUtil.getUserId();
        System.out.println(userId);

        List<LabelsDTO> labelsDTOS = labelsService.readLabelsByUserId(userId);
        System.out.println(labelsDTOS);
        return ResponseEntity.ok(labelsDTOS);
    }

    @Operation(
            summary = "라벨 등록",
            description = "라벨을 등록합니다",
            tags = {"POST"}
    )
    @PostMapping
    public ResponseEntity createLabelsByUserId(@RequestBody LabelsDTO labels){

        Integer userId = securityUtil.getUserId();
        System.out.println(userId);

        String result = labelsService.createLabelsByUserId(labels, userId);
        return ResponseEntity.ok(result);
    }

//    @Operation(
//            summary = "라벨 수정",
//            description = "라벨을 수정합니다",
//            tags = {"PUT"}
//    )
//    @PutMapping
//    public ResponseEntity updateLabelsByLabelsId(@RequestBody LabelsDTO labels){
//
//        String result = labelsService.updateLabelByLabelsId(labels);
//        return ResponseEntity.ok(result);
//    }

    @Operation(
            summary = "라벨 삭제",
            description = "라벨을 삭제합니다",
            tags = {"PUT"}
    )
    @PutMapping("/delete")
    public ResponseEntity deleteLabelsByLabelsId(@RequestParam("labelsId")Integer labelsId){
        String result = labelsService.deleteLabelsByLabelsId(labelsId);

        return ResponseEntity.ok(result);
    }

}
