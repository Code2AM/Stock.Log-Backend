package com.code2am.stocklog.domain.notes.models.entity;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotesTest {

    private Notes notes; // 클래스 수준에서 Notes 객체 선언

    @BeforeEach
    void setup(){
        // Note 객체를 여기서 초기화
        LocalDateTime now = LocalDateTime.now();
        notes = new Notes(); // 이제 전체 클래스에서 사용할 수 있습니다.
        notes.setNoteId(1);
        notes.setNoteName("Test Note");
        notes.setNoteContents("This is a test note content.");
        notes.setNoteDate(now);
        notes.setNoteStatus("Active");
        notes.setUserId(101);
    }

    @Test
    public void testConvertToDTO() {
        // Given: 사전 설정 단계
        // Labels에 대한 모의 객체 생성
        Labels mockLabels = mock(Labels.class);

        // MOCK : Labels의 convertToDTO() 메서드가 호출될 때 반환할 객체 설정
        when(mockLabels.convertToDTO()).thenReturn(new LabelsDTO());

        // setup에서 생성된 notes 객체에 Labels 설정
        notes.setLabels(mockLabels);


        // When: 실행 단계
        NotesDTO notesDTO = notes.convertToDTO();

        // Then: 검증 단계
        // dto가 null인지 아닌지 검증
        assertNotNull(notesDTO);
        assertEquals(notes.getNoteId(), notesDTO.getNoteId());
        assertEquals(notes.getNoteName(), notesDTO.getNoteName());
        assertEquals(notes.getNoteContents(), notesDTO.getNoteContents());
        assertEquals(notes.getNoteDate(), notesDTO.getNoteDate());
        assertEquals(notes.getNoteStatus(), notesDTO.getNoteStatus());
        assertEquals(notes.getUserId(), notesDTO.getUserId());
        // LabelsDTO가 null이 아닌지 검증
        assertNotNull(notesDTO.getLabelsDTO());

        // Labels.convertToDTO() 메서드가 한 번 호출되었는지 확인
        verify(mockLabels, times(1)).convertToDTO();
    }
}