package com.code2am.stocklog.domain.journals.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.journals.service.JournalsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/journals")
@Tag(name = "매매일지 API", description = "매매일지를 관리하는 API")
public class JournalsController {

    @Autowired
    private JournalsService journalsService;

    @Autowired
    private AuthUtil authUtil;

    @Operation(
            summary = "매매일지 등록",
            description = "입력값을 받아 매매일지를 등록합니다.",
            tags = {"POST"}
    )
    @Parameter(name = "journals", description = "새롭게 만들 매매일지의 데이터")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매일지 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값"),
            @ApiResponse(responseCode = "401", description = "사용자 정보 미획득")
    })
    @PostMapping
    public ResponseEntity<String> createJournalsByUserId(@Valid @RequestBody JournalsDTO journals){

        Integer userId = authUtil.getUserId();

        if(userId <= 0){
            return ResponseEntity.status(401).body("사용자 정보를 가져올 수 없습니다.");
        }

        String result = journalsService.createJournalsByUserId(journals, userId);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 조회",
            description = "유저 정보를 받아 매매일지를 조회합니다.",
            tags = {"GET"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매일지 조회 성공"),
            @ApiResponse(responseCode = "401", description = "사용자 정보 미획득")
    })
    @GetMapping
    public ResponseEntity readJournalsByUserId(){

        Integer userId = authUtil.getUserId();
        System.out.println(userId);

        if(userId == null){
            return ResponseEntity.status(401).body("사용자 정보를 획득할 수 없습니다.");

        }
        List<JournalsDTO> result = journalsService.readJournalsByUserId(userId);
        System.out.println(result);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 삭제",
            description = "매매일지를 삭제합니다.",
            tags = {"POST"}
    )
    @Parameter(name = "journals", description = "삭제할 매매일지의 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매일지 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "매매일지 정보 없음"),
            @ApiResponse(responseCode = "404", description = "삭제할 매매일지가 없음"),
            @ApiResponse(responseCode = "500", description = "이미 삭제된 매매일지입니다.")
    })
    @PostMapping("/delete")
    public ResponseEntity<String> deleteJournalsByJournalsId(@RequestBody JournalsDTO journals){

        if(journals.getJournalId() == null){
            System.out.println("요청이 전달 안됨");
            return ResponseEntity.badRequest().body("요청값이 없습니다.");
        }

        Integer journalId = journals.getJournalId();

        String result = journalsService.deleteJournalsByJournalsId(journalId);

        if(result.equals("매매일지 정보가 정상적으로 넘어오지 않았습니다.")){
            return ResponseEntity.status(400).body("매매일지 정보가 정상적으로 넘어오지 않았습니다.");
        }

        if(result.equals("이미 삭제된 매매일지입니다.")){
            return ResponseEntity.status(500).body("이미 삭제된 매매일지입니다.");
        }

        if(result.equals("조회된 결과가 없습니다.")){
            return ResponseEntity.status(404).body("조회된 결과가 없습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 거래 상태 변경",
            description = "매매일지의 거래 상태를 종료합니다.",
            tags = {"POST"}
    )
    @Parameter(name = "journalsDTO", description = "수정할 매매일지 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매일지 상태 변경 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 입력"),
            @ApiResponse(responseCode = "404", description = "해당 매매일지 없음"),
            @ApiResponse(responseCode = "500", description = "이미 바뀐 상태")
    })
    @PostMapping("/change")
    public ResponseEntity<String> updateJournalsStatusByJournalId(@RequestBody JournalsDTO journalsDTO){

        Integer journalId = journalsDTO.getJournalId();

        if(journalId == null){
            return ResponseEntity.badRequest().body("매매일지가 입력되지 않았습니다.");
        }

        String result = journalsService.updateJournalsStatusByJournalId(journalId);

        if(result.equals("이미 닫힌 매매일지입니다.")){
            return ResponseEntity.status(500).body("이미 닫힌 매매일지입니다.");
        }
        if(result.equals("없는 매매일지입니다.")){
            return ResponseEntity.status(404).body("없는 매매일지입니다.");
        }

        return ResponseEntity.ok(result);

    }
}
