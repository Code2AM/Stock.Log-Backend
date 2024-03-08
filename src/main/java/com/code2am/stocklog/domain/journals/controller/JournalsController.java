package com.code2am.stocklog.domain.journals.controller;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
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
}
