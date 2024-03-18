package com.code2am.stocklog.domain.notes.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.notes.repository.NotesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class NotesServiceTest {

    @InjectMocks
    private NotesService notesService;

    @Mock
    private NotesRepository notesRepository;

    @Mock
    AuthUtil authUtil;


    /* readNotesByUserId */
    @Test
    void readNotesByUserId() {
    }

    // 성공 정상적으로 notesDTOList 반환

    // 성공 - 빈 리스트 를 반환

    // authUtil 관련 문제

    // database, jpa 관련 예외


    /* createNoteByUserId */
    @Test
    void createNoteByUserId() {
    }

    // 성공 정상적으로 등록됨

    // authUtil 관련 문제 - 인증된 사용자가 없습니다

    // database, jpa 관련 문제



    /* deleteNoteByNoteId */
    @Test
    void deleteNoteByNoteId() {
    }

    // 성공 정상적으로 삭제됨

    // 데이터베이스 저장 실패




    /* updateNoteByNoteId */
    @Test
    void updateNoteByNoteId() {
    }
    // 성공 정상적으로 변경됨

    // 데이터베이스 저장 실패
}