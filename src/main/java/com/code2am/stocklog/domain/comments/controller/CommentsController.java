package com.code2am.stocklog.domain.comments.controller;

import com.code2am.stocklog.domain.comments.models.dto.CommentsDTO;
import com.code2am.stocklog.domain.comments.models.entity.Comments;
import com.code2am.stocklog.domain.comments.models.vo.CommentsVO;
import com.code2am.stocklog.domain.comments.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            tags = {"POST"}
    )
    @PostMapping
    public ResponseEntity createCommentByJournalId(@RequestBody Comments comments){

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
            tags = {"POST"}
    )
    @PostMapping("/read")
    public List<CommentsVO> readCommentsByJournalId(@RequestBody CommentsDTO comments){

        System.out.println(comments);

        if(Objects.isNull(comments)){
            return null;
        }

        Integer journalId = comments.getJournalId();

        System.out.println("코멘트 조회 " + journalId);

        return commentsService.readCommentsByJournalId(journalId);
    }

    @Operation(
            summary = "코멘트 삭제",
            description = "매매일지의 코멘트를 삭제합니다.",
            tags = {"POST"}
    )
    @PostMapping("/delete")
    public ResponseEntity deleteCommentByCommentId(@RequestBody CommentsDTO commentsDTO){

        if(Objects.isNull(commentsDTO)){
            return ResponseEntity.badRequest().body("삭제할 코멘트가 없습니다.");
        }

        Integer commentId = commentsDTO.getCommentId();
        String result = commentsService.deleteCommentByCommentId(commentId);

        return ResponseEntity.ok(result);
    }
}
