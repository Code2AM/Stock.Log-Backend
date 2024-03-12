package com.code2am.stocklog.domain.journals.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.models.entity.Journals;
import com.code2am.stocklog.domain.journals.service.JournalsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping
    public ResponseEntity createJournalsByUserId(@RequestBody JournalsDTO journals){

        System.out.println(journals);
        if(Objects.isNull(journals)){
            ResponseEntity.badRequest().body("잘못된 입력값입니다.");
        }

        Integer userId = authUtil.getUserId();
        System.out.println(userId);

        String result = journalsService.createJournalsByUserId(journals, userId);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 조회",
            description = "유저 정보를 받아 매매일지를 조회합니다.",
            tags = {"GET"}
    )
    @GetMapping
    public ResponseEntity readJournalsByUserId(){

        Integer userId = authUtil.getUserId();
        System.out.println(userId);

        List<JournalsDTO> result = journalsService.readJournalsByUserId(userId);
        System.out.println(result);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 삭제",
            description = "매매일지를 삭제합니다.",
            tags = {"POST"}
    )
    @PostMapping("/delete")
    public ResponseEntity deleteJournalsByJournalsId(@RequestBody JournalsDTO journals){

        if(Objects.isNull(journals)){
            System.out.println("요청이 전달 안됨");
            return ResponseEntity.badRequest().body("값이 없습니다.");
        }

        System.out.println("삭제를 위한 요청" + journals.getJournalId());

        Integer journalId = journals.getJournalId();

        String result = journalsService.deleteJournalsByJournalsId(journalId);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "매매일지 거래 상태 변경",
            description = "매매일지의 거래 상태를 종료합니다.",
            tags = {"POST"}
    )
    @PostMapping("/change")
    public ResponseEntity updateJournalsStatusByJournalId(@RequestBody JournalsDTO journalsDTO){

        if(Objects.isNull(journalsDTO)){
            System.out.println("요청 전달이 안됨");
            return ResponseEntity.badRequest().body("매매일지가 입력되지 않았습니다.");
        }

        Integer journalId = journalsDTO.getJournalId();

        String result = journalsService.updateJournalsStatusByJournalId(journalId);

        return ResponseEntity.ok(result);

    }
}
