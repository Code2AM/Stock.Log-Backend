package com.code2am.stocklog.domain.notes.controller;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.notes.models.vo.NotesVo;
import com.code2am.stocklog.domain.notes.service.NotesService;
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
@RequestMapping("/notes")
@Tag(name = "매매노트 관리 API", description = "매매 노트를 관리하는 API")
public class NotesController {

    @Autowired
    public NotesService notesService;


    /* 사용자의 모든 Notes를 불러오는 메소드 */
    /**
     * 매매일지에 해당하는 매매노트를 조회하는 메소드
     * @return 매매일지에 해당되는 모든 매매노트
     * */
    @Operation(
            summary = "매매노트 조회",
            description = "매매일지의 PrimaryKey 값과 노트의 상태가 'Y'인 조건으로 매매노트를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매노트를 성공적으로 조회함."),
            @ApiResponse(responseCode = "404", description = "해당하는 매매일지가 존재하지 않음."),
            @ApiResponse(responseCode = "500", description = "서버가 원할히 동작하지 않거나 DB의 값이 존재하지 않음.")
    })
    @GetMapping ("/allNotes")
    public ResponseEntity<List<NotesDTO>> readNotesByUserId(){
        return ResponseEntity.ok(notesService.readNotesByUserId());
    }


    /* 신규 노트를 만드는 메소드*/
    /**
     * 매매노트를 입력받는 메소드
     * @param notesDTO
     * @return 신규 매매노트를 입력합니다.
     * */
    @Operation(
            summary = "노트 등록",
            description = "신규 노트를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "노트를 성공적으로 등록함."),
            @ApiResponse(responseCode = "400", description = "요청에 필요한 값이 정상적으로 입력되지 앟음."),
            @ApiResponse(responseCode = "500", description = "요청받은 서버가 정상적으로 동작하지 않음.")
    })
    @Parameter(name = "notesDTO", description = "매매일지에 신규로 등록할 매매노트")
    @PostMapping("/create")
    public ResponseEntity<String> createNoteByJournalId(
            @Valid @RequestBody NotesDTO notesDTO){
        // 매매일지에서 이용자의 요청을 받아 해당 일지에 노트를 작성한다. 매매일지의 키 값을 필수로 요구한다.

        System.out.println(notesDTO);
        // 요청값이 없는지 확인
        if(Objects.isNull(notesDTO.getNoteName())){
            return ResponseEntity.badRequest().body("제목이 없습니다.");
        }

//        if(Objects.isNull(notesDTO.getUserId())){
//            return ResponseEntity.badRequest().body("인증된 사용자가 없습니다");
//        }

        // 실제 입력받은 데이터를 입력 로직으로
        String result = notesService.createNoteByUserId(notesDTO);

        // 요청이 원활히 도착했는지 확인
        if(Objects.isNull(result)){
            return ResponseEntity.status(500).body("서버의 통신이 원할치 못합니다.");
        }

        System.out.println(result);
        return ResponseEntity.ok("등록에 성공하였습니다.");
    }



    /* Note를 삭제하는 메소드 */
    /**
     * 해당 매매노트를 삭제하는 메소드
     * @param notesDTO
     * @return 해당 매매노트를 삭제함
     * */
    @Operation(
            summary = "노트 삭제",
            description = "이미 존재하고 있는 매매노트를 삭제합니다."
    )
    @Parameter(name = "notesDTO", description = "매매노트에 해당하는 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "노트를 삭제함."),
            @ApiResponse(responseCode = "400", description = "삭제하고자 하는 노트 정보 오입력"),
            @ApiResponse(responseCode = "404", description = "삭제하고자 하는 노트 없음")

    })
    @PostMapping("/delete")
    public ResponseEntity<String> deleteNoteByNoteId(@RequestBody NotesDTO notesDTO){

        if(Objects.isNull(notesDTO.getNoteId())){
            return ResponseEntity.badRequest().body("노트가 없습니다.");
        }
        System.out.println("컨트롤러 도착");
        // 실제로는 삭제 메카니즘이 아니라 상태를 수정함
        notesService.deleteNoteByNoteId(notesDTO);
        return ResponseEntity.ok("삭제 완료");
    }


    /* Note를 수정하는 메소드 */
    @Operation(
            summary = "노트 수정",
            description = "이미 존재하고 있는 매매노트를 수정합니다."
    )
    @Parameter(name = "notesDTO", description = "수정하고자 하는 노트에 대한 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "매매노트를 삭제함."),
            @ApiResponse(responseCode = "400", description = "수정하고자 하는 노트의 내용 오입력"),
            @ApiResponse(responseCode = "404", description = "수정하고자 하는 노트 없음")
    })
    @PostMapping("/update")
    public ResponseEntity<String> updateNoteByNoteId(@Valid @RequestBody NotesDTO notesDTO){
        if(Objects.isNull(notesDTO.getNoteId())){
            return ResponseEntity.badRequest().body("노트가 없습니다.");
        }
        // 실제로는 삭제 메카니즘이 아니라 상태를 수정함
        System.out.println(notesDTO);
        System.out.println("컨트롤러 도착");
        notesService.updateNoteByNoteId(notesDTO);
        return ResponseEntity.ok("수정 성공");
    }

}