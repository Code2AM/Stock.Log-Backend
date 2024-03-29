package com.code2am.stocklog.domain.notes.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.labels.dao.LabelsDAO;
import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.dao.NotesDAO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.repository.NotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    AuthUtil authUtil;


    /* 사용자의 Notes를 전부 불러온다 */
    /**
     * 매매일지의 id 값을 받아 그에 해당하는 매매노트를 전부 조회하는 메소드
     * */
    public List<NotesDTO> readNotesByUserId() {

        List<NotesDTO> notesDTOS = new ArrayList<>();

        if (authUtil.getUserId() == null) {
            throw new NullPointerException("userId가 null");
        }

        notesRepository.findAllByUserId(authUtil.getUserId())
                .forEach(note -> {
                    notesDTOS.add(note.convertToDTO());
                });

        return notesDTOS;
    }

    /**
     * 매매일지의 id 값을 받아 매매노트를 생성하는 메소드
     * */
    public String createNoteByUserId(NotesDTO notesDTO) {

        if(notesDTO.getNoteName().isEmpty()){
            return "제목이 없습니다.";
        }

        // DTO를 앤티티에 담아 JPA를 통해 등록
        Notes newNote = notesDTO.convertToEntity();

        newNote.setUserId(authUtil.getUserId());
        newNote.setNoteDate(LocalDateTime.now());
        newNote.setNoteStatus("Y");

        notesRepository.save(newNote);

        return "성공";
    }


    /**
     * 매매일지를 삭제하는 메소드
     * */
    public String deleteNoteByNoteId(NotesDTO notesDTO) {

        // 실제로 삭제하는 것이 아니라 단지 상태값을 N으로 만드는 것
        // DTO를 앤티티에 담아 넘긴다.
        Notes deleteNote = notesDTO.convertToEntity();
        deleteNote.setNoteStatus("N");

        notesRepository.save(deleteNote);
        return "삭제 완료";
    }

    /* 노트의 내용을 변경하는 메소드 */
    public String updateNoteByNoteId(NotesDTO notesDTO) {

        notesDTO.setNoteDate(LocalDateTime.now());
        notesRepository.save(notesDTO.convertToEntity());
        return "수정 성공";
    }
}
