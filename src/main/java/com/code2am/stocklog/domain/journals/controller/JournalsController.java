package com.code2am.stocklog.domain.journals.controller;

import com.code2am.stocklog.domain.auth.common.util.SecurityUtil;
import com.code2am.stocklog.domain.journals.models.dto.JournalsDTO;
import com.code2am.stocklog.domain.journals.service.JournalsService;
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
@RequestMapping("/journals")
@Tag(name = "매매일지 API", description = "매매일지를 관리하는 API")
public class JournalsController {

    @Autowired
    private JournalsService journalsService;

    @Autowired
    private SecurityUtil securityUtil;

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

        Integer userId = securityUtil.getUserId();
        System.out.println(userId);

        String result = journalsService.createJournalsByUserId(journals, userId);

        return ResponseEntity.ok(result);
    }
}
