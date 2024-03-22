package com.code2am.stocklog.domain.labels.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.service.LabelsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/labels")
@Tag(name = "라벨 API", description = "라벨을 관리하는 API")
public class LabelsController {

    @Autowired
    LabelsService labelsService;

    @Autowired
    AuthUtil authUtil;

    /* 라벨 조회 */
    @Operation(
            summary = "라벨 조회",
            description = "사용자의 라벨을 조회합니다"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자의 라벨 정보를 조회")
    })
    @GetMapping("/get")
    public ResponseEntity<List<LabelsDTO>> readLabelsByUserId(){

        List<LabelsDTO> labelsDTOS = labelsService.readLabelsByUserId();

        System.out.println(labelsDTOS);

        return ResponseEntity.ok(labelsDTOS);
    }


    /* 라벨 등록 */
    @Operation(
            summary = "라벨 등록",
            description = "라벨을 등록합니다"
    )
    @Parameter(name = "labels", description = "입력하고자 하는 라벨의 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라벨 등록 성공"),
            @ApiResponse(responseCode = "400", description = "라벨 입력에 필요한 정보 오입력")
    })
    @PostMapping("/create")
    public ResponseEntity<String> createLabelsByUserId(@Valid  @RequestBody LabelsDTO labels){

        String result = labelsService.createLabelsByUserId(labels);
        return ResponseEntity.ok(result);
    }


    /* 라벨 수정 */
    @Operation(
            summary = "라벨 수정",
            description = "라벨을 수정합니다"
    )
    @Parameter(name = "labels", description = "라벨을 수정하기 위해 필요한 해당 라벨의 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라벨 정보 수정 성공"),
            @ApiResponse(responseCode = "400", description = "라벨 수정에 필요한 정보 오입력"),
            @ApiResponse(responseCode = "404", description = "변경하고자 하는 라벨이 없음")
    })
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
            description = "라벨을 삭제합니다"
    )
    @Parameter(name = "labels", description = "삭제하고자 하는 라벨의 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "라벨 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "라벨 삭제를 위한 정보 오입력"),
            @ApiResponse(responseCode = "404", description = "삭제하고자 하는 라벨이 없음")
    })
    @PostMapping("/delete")
    public ResponseEntity<String> deleteLabelsByLabelsId(@RequestBody LabelsDTO labels){

        String result = labelsService.deleteLabelsByLabelsId(labels);

        return ResponseEntity.ok(result);
    }

}
