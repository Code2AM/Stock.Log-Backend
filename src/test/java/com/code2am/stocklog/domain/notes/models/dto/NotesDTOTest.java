package com.code2am.stocklog.domain.notes.models.dto;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotesDTOTest {

    private NotesDTO notesDTO;

    @BeforeEach
    void setup(){
        notesDTO = new NotesDTO(); // 이제 전체 클래스에서 사용할 수 있습니다.
        notesDTO.setNoteId(1);
        notesDTO.setNoteName("Test Note");
        notesDTO.setNoteContents("This is a test note content.");
        notesDTO.setNoteDate(LocalDateTime.now());
        notesDTO.setNoteStatus("Active");
        notesDTO.setUserId(101);
    }

    @Test
    void convertToEntity() {
        // GIVEN
        LabelsDTO mockLabelsDTO = mock(LabelsDTO.class);

        // MOCK : 반환 Labels
        when(mockLabelsDTO.convertToEntity()).thenReturn(new Labels());

        // setup 에서  생성된 notes 객체에 LabelsDTO 설정
        notesDTO.setLabelsDTO(mockLabelsDTO);

        // WHEN
        Notes note = notesDTO.convertToEntity();

        // THEN
        assertNotNull(note);
        assertEquals(notesDTO.getNoteId(), note.getNoteId());
        assertEquals(notesDTO.getNoteName(), note.getNoteName());
        assertEquals(notesDTO.getNoteContents(), note.getNoteContents());
        assertEquals(notesDTO.getNoteDate(), note.getNoteDate());
        assertEquals(notesDTO.getNoteStatus(), note.getNoteStatus());
        assertEquals(notesDTO.getUserId(), note.getUserId());
        // LabelsDTO가 null이 아닌지 검증
        assertNotNull(notesDTO.getLabelsDTO());

        // Labels.convertToDTO() 메서드가 한 번 호출되었는지 확인
        verify(mockLabelsDTO, times(1)).convertToEntity();

    }
}