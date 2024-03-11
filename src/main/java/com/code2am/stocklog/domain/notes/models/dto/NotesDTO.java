package com.code2am.stocklog.domain.notes.models.dto;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotesDTO {
    // 매매노트 PK
    private Integer noteId;
    // 노트 이름
    private String noteName;
    // 매매노트 내용
    private String noteContents;
    // 매매노트 등록 날짜
    private LocalDateTime noteDate;
    // 매매노트 상태
    private String noteStatus;
    // 유저아이디 FK
    private Integer userId;
    // 라벨
    private LabelsDTO labelsDTO;

    /* Entity Converter */
    public Notes convertToEntity() {
        Notes note = new Notes();
        note.setNoteId(this.noteId);
        note.setNoteContents(this.noteContents);
        note.setNoteDate(this.noteDate);
        note.setNoteName(this.noteName);
        note.setNoteStatus(this.noteStatus);
        note.setUserId(this.userId);

        return note;
    }
}
