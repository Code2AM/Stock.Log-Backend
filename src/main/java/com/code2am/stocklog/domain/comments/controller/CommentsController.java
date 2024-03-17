package com.code2am.stocklog.domain.comments.controller;

import com.code2am.stocklog.domain.comments.models.dto.CommentsRequestDTO;
import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import com.code2am.stocklog.domain.comments.service.CommentsService;
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
@RequestMapping("/comments")
@Tag(name = "코멘트 API", description = "매매일지에 들어가는 코멘트를 관리하는 API")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @Operation(
            summary = "코멘트 등록",
            description = "매매일지에 코멘트를 등록합니다.",
            tags = {"create"}
    )
    @Parameter(name = "comments", description = "사용자가 입력한 코멘트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코멘트 등록에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.")
    })
    @PostMapping
    public ResponseEntity<String> createCommentByJournalId(@Valid @RequestBody CommentsRequestDTO comments){

        if(Objects.isNull(comments)){
            return ResponseEntity.badRequest().body("잘못된 입력입니다.");
        }

        String result = commentsService.createComment(comments);

        if(result.isEmpty()){
            return ResponseEntity.badRequest().body("코멘트가 비어 있습니다.");
        }

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "코멘트 조회",
            description = "매매일지의 코멘트를 조회합니다.",
            tags = {"read"}
    )
    @Parameter(name = "comments", description = "매매일지의 번호가 등록된 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코멘트가 전부 조회되었습니다."),
            @ApiResponse(responseCode = "400", description = "입력된 정보가 없습니다.")
    })
    @PostMapping("/read")
    public List<CommentsVO> readCommentsByJournalId(@RequestBody CommentsRequestDTO comments){

        System.out.println(comments);

        if(Objects.isNull(comments)){
            return null;
        }

        Integer journalId = comments.getJournalId();

        return commentsService.readCommentsByJournalId(journalId);
    }

    @Operation(
            summary = "코멘트 삭제",
            description = "매매일지의 코멘트를 삭제합니다.",
            tags = {"delete"}
    )
    @Parameter(name = "comments", description = "코멘트의 번호를 담은 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "코멘트를 삭제하였습니다."),
            @ApiResponse(responseCode = "400", description = "입력된 정보가 없습니다."),
            @ApiResponse(responseCode = "404", description = "해당하는 코멘트를 찾을 수 없습니다.")
    })
    @PostMapping("/delete")
    public ResponseEntity<String> deleteCommentByCommentId(@RequestBody CommentsRequestDTO comments){

        if(Objects.isNull(comments)){
            return ResponseEntity.badRequest().body("입력된 정보가 없습니다.");
        }

        Integer commentId = comments.getCommentId();
        String result = commentsService.deleteCommentByCommentId(commentId);

        return ResponseEntity.ok(result);
    }
}
