package com.code2am.stocklog.domain.notes.service;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.notes.repository.NotesRepository;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @InjectMocks
    private NotesService notesService;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    AuthUtil authUtil;


    /* readNotesByUserId */
    // 성공 정상적으로 notesDTOList 반환
    @Test
    void readNoteByUserId_성공(){
        List<Notes> fakeNotesList = new ArrayList<>();
        Labels fakeLabel = new Labels();
        fakeLabel.setLabelsTitle("테스트 라벨");

        Notes fakeNote1 = new Notes(); // 예시로 Notes 클래스라 가정
        fakeNote1.setNoteId(1);
        fakeNote1.setNoteName("Note 1");
        fakeNote1.setNoteContents("Content 1");
        fakeNote1.setLabels(fakeLabel);
        fakeNotesList.add(fakeNote1);

        // Mockito를 사용하여 findAllByUserId() 메소드의 가짜 동작 설정
        when(authUtil.getUserId()).thenReturn(1); // 가짜 유저 아이디 설정
        when(notesRepository.findAllByUserId(1)).thenReturn(fakeNotesList);

        // 테스트할 메소드 호출
        List<NotesDTO> result = notesService.readNotesByUserId();

        // 예상 결과 생성
        List<NotesDTO> expected = new ArrayList<>();
        for (Notes note : fakeNotesList) {
            expected.add(note.convertToDTO());
        }

        // 결과 비교
        assertEquals(expected, result);
    }

    // 성공 - 빈 리스트 를 반환
    @Test
    void readNoteByUserId_성공2(){

        List<Notes> emptyNotesList = new ArrayList<>();
        // Mockito를 사용하여 findAllByUserId() 메소드의 가짜 동작 설정
        when(authUtil.getUserId()).thenReturn(1); // 가짜 유저 아이디 설정
        when(notesRepository.findAllByUserId(1)).thenReturn(emptyNotesList);

        // 테스트할 메소드 호출
        List<NotesDTO> result = notesService.readNotesByUserId();

        // 결과 비교 - 빈 리스트인지 확인
        assertEquals(emptyNotesList.size(), result.size());
    }

    // authUtil 관련 문제
    // userId가 null일 경우
    @Test
    void readNoteByUserId_userId_is_null(){
        // Mockito를 사용하여 authUtil.getUserId() 메소드 호출 시 null 반환하도록 설정
        when(authUtil.getUserId()).thenReturn(null);

        // NullPointerException이 발생하는지 확인
        assertThrows(NullPointerException.class, () -> {
            notesService.readNotesByUserId();
        });
    }



    /* createNoteByUserId */

    // 성공 정상적으로 등록됨
    @Test
    @DisplayName("등록 성공")
    public void createNoteByUserId_성공(){
        // given
        LabelsDTO fakeLabelDTO = new LabelsDTO();
        fakeLabelDTO.setLabelsTitle("테스트 라벨");

        // 전달받은 dto
        NotesDTO notesDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(fakeLabelDTO)
                .build();

        Labels fakeLabel = fakeLabelDTO.convertToEntity();
        Notes expectedNotes = new Notes();
        expectedNotes.setNoteName("note1");
        expectedNotes.setNoteContents("content");
        expectedNotes.setNoteStatus("Y");
        expectedNotes.setUserId(1);
        expectedNotes.setLabels(fakeLabel);

        given(authUtil.getUserId()).willReturn(1);
        given(notesRepository.save(any(Notes.class))).willReturn(expectedNotes);

        //when
        String result = notesService.createNoteByUserId(notesDTO);

        // then
        assertEquals(result, "성공");
    }

    // authUtil 관련 문제 - 인증된 사용자가 없습니다
//    @Test
//    @DisplayName("인증된 사용자가 없는 경우")
//    public void createNoteByUserId_실패_1() {
//        // given
//        LabelsDTO fakeLabel = new LabelsDTO();
//        fakeLabel.setLabelsTitle("테스트 라벨");
//
//        NotesDTO notesDTO = NotesDTO.builder()
//                .noteName("제목")
//                .noteContents("내용")
//                .labelsDTO(fakeLabel)
//                .build();
//
//        String expectedExceptionMessage = "인증된 사용자가 없습니다";
//
//        // stub
//        given(authUtil.getUserId()).willThrow(new AuthUtilException(expectedExceptionMessage));
//
//        // when & then
//        try {
//            notesService.createNoteByUserId(notesDTO);
//        } catch (AuthUtilException e) {
//            assertEquals(expectedExceptionMessage, e.getMessage());
//        }
//    }

    // database, jpa 관련 문제
    @Test
    @DisplayName("노트 생성 JPA 저장 실패")
    void createNoteByUserId_JPA_실패() {
        // given
        LabelsDTO fakeLabelDTO = new LabelsDTO();
        fakeLabelDTO.setLabelsTitle("테스트 라벨");

        NotesDTO notesDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(fakeLabelDTO)
                .build();

        // 예상되는 예외
        DataIntegrityViolationException expectedException = new DataIntegrityViolationException("저장 실패: 데이터베이스 제약 조건 위반");

        // notesRepository.save() 메소드가 예외를 던지도록 설정
        given(notesRepository.save(any(Notes.class))).willThrow(expectedException);

        // when & then
        // 예외가 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> {
            notesService.createNoteByUserId(notesDTO);
        });
    }



    /* deleteNoteByNoteId */
    // 성공 정상적으로 삭제됨
    @Test
    @DisplayName("삭제 성공")
    void deleteNoteByNoteId_성공(){

        //given

        // 노트에 필요한 라벨
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .build();


        // 노트 DTO
        NotesDTO noteDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(labelsDTO)
                .build();

        // when
        String result = notesService.deleteNoteByNoteId(noteDTO);

        // then
        assertEquals("삭제 완료", result);
    }

    // 데이터베이스 저장 실패
    @Test
    @DisplayName("노트 삭제 JPA 저장 실패")
    void deleteNoteByNoteId_JPA_실패() {
        // given
        LabelsDTO fakeLabelDTO = new LabelsDTO();
        fakeLabelDTO.setLabelsTitle("테스트 라벨");

        NotesDTO notesDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(fakeLabelDTO)
                .build();

        // 예상되는 예외
        DataIntegrityViolationException expectedException = new DataIntegrityViolationException("저장 실패: 데이터베이스 제약 조건 위반");

        // notesRepository.save() 메소드가 예외를 던지도록 설정
        given(notesRepository.save(any(Notes.class))).willThrow(expectedException);

        // when & then
        // 예외가 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> {
            notesService.deleteNoteByNoteId(notesDTO);
        });
    }


    /* updateNoteByNoteId */

    // 성공 정상적으로 변경됨
    @Test
    @DisplayName("수정 성공")
    void updateNoteByNoteId_성공(){

        //given

        // 노트에 필요한 라벨
        LabelsDTO labelsDTO = LabelsDTO.builder()
                .labelsTitle("Label1")
                .build();


        // 노트 DTO
        NotesDTO noteDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(labelsDTO)
                .build();

        // when
        String result = notesService.updateNoteByNoteId(noteDTO);

        // then
        assertEquals("수정 성공", result);
    }

    // 데이터베이스 저장 실패
    @Test
    @DisplayName("노트 수정 JPA 저장 실패")
    void updateNoteByNoteId_JPA_실패() {
        // given
        LabelsDTO fakeLabelDTO = new LabelsDTO();
        fakeLabelDTO.setLabelsTitle("테스트 라벨");

        NotesDTO notesDTO = NotesDTO.builder()
                .noteName("note1")
                .noteContents("content")
                .labelsDTO(fakeLabelDTO)
                .build();

        // 예상되는 예외
        DataIntegrityViolationException expectedException = new DataIntegrityViolationException("저장 실패: 데이터베이스 제약 조건 위반");

        // notesRepository.save() 메소드가 예외를 던지도록 설정
        given(notesRepository.save(any(Notes.class))).willThrow(expectedException);

        // when & then
        // 예외가 발생하는지 확인
        assertThrows(DataIntegrityViolationException.class, () -> {
            notesService.updateNoteByNoteId(notesDTO);
        });
    }
}