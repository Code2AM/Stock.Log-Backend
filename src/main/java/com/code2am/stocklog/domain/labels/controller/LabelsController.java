package com.code2am.stocklog.domain.labels.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.service.LabelsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    /* 라벨 조회 */
    @Operation(
            summary = "라벨 조회",
            description = "사용자의 라벨을 조회합니다",
            tags = {"POST"}
    )
    @PostMapping("/get")
    public ResponseEntity<List<LabelsDTO>> readLabelsByUserId(){

        List<LabelsDTO> labelsDTOS = labelsService.readLabelsByUserId();

        System.out.println(labelsDTOS);

        return ResponseEntity.ok(labelsDTOS);
    }


    /* 라벨 등록 */
    @Operation(
            summary = "라벨 등록",
            description = "라벨을 등록합니다",
            tags = {"POST"}
    )
    @PostMapping("/create")
    public ResponseEntity<String> createLabelsByUserId(@Valid  @RequestBody LabelsDTO labels){

        String result = labelsService.createLabelsByUserId(labels);
        return ResponseEntity.ok(result);
    }


    /* 라벨 수정 */
    @Operation(
            summary = "라벨 수정",
            description = "라벨을 수정합니다",
            tags = {"PUT"}
    )
    @PostMapping("/update")
    public ResponseEntity<String> updateLabelsByLabelsId(@Valid @RequestBody LabelsDTO labels){
        System.out.println("수정 도착");
        System.out.println(labels);
        String result = labelsService.updateLabelByLabelsId(labels);
        System.out.println(result);
        return ResponseEntity.ok(result);
    }


    /* 라벨 삭제 */
    @Operation(
            summary = "라벨 삭제",
            description = "라벨을 삭제합니다",
            tags = {"POST"}
    )
    @PostMapping("/delete")
    public ResponseEntity<String> deleteLabelsByLabelsId(@RequestBody LabelsDTO labels){
        String result = labelsService.deleteLabelsByLabelsId(labels);

        return ResponseEntity.ok(result);
    }

}
