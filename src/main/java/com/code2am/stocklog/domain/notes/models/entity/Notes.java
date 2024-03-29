package com.code2am.stocklog.domain.notes.models.entity;

import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "TBL_NOTES")
public class Notes {

    @Id
    @Column(name = "NOTE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noteId;

    @Column(name = "NOTE_NAME")
    private String noteName;

    @Column(name = "NOTE_CONTENTS")
    private String noteContents;

    @Column(name = "NOTE_DATE")
    @Temporal(value = TemporalType.TIMESTAMP)
    private LocalDateTime noteDate;

    @Column(name = "NOTE_STATUS")
    private String noteStatus;

    @Column(name = "USER_ID")
    private Integer userId;

    @ManyToOne(optional = true)
    private Labels labels;


    /* DTO Converter */
    public NotesDTO convertToDTO() {
        NotesDTO noteDTO = new NotesDTO();
        noteDTO.setNoteId(this.noteId);
        noteDTO.setNoteContents(this.noteContents);
        noteDTO.setNoteDate(this.noteDate);
        noteDTO.setNoteName(this.noteName);
        noteDTO.setNoteStatus(this.noteStatus);
        noteDTO.setUserId(this.userId);
        noteDTO.setLabelsDTO(this.labels.convertToDTO());
        return noteDTO;
    }

}
