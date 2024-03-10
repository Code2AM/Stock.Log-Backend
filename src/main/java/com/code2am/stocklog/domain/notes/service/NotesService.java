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
    private NotesDAO notesDAO;

    @Autowired
    AuthUtil authUtil;

    @Autowired
    private LabelsDAO labelsDAO;

    /* 사용자의 Notes를 전부 불러온다 */
    /**
     * 매매일지의 id 값을 받아 그에 해당하는 매매노트를 전부 조회하는 메소드
     * */
    public List<NotesDTO> readNotesByUserId() {

        List<NotesDTO> notesDTOS = new ArrayList<>();

        notesRepository.findAllByUserId(authUtil.getUserId()).forEach(note -> {
            notesDTOS.add(note.convertToDTO());
        });

        return notesDTOS;
    }

    /**
     * 매매일지의 id 값을 받아 매매노트를 생성하는 메소드
     * */
    public Notes createNoteByUserId(NotesDTO notesDTO, Integer labelsId) {

        LabelsDTO labelDTO = labelsDAO.readLabelsByLabelsId(labelsId);
        if (labelDTO == null) {
            throw new RuntimeException("labelsId에 해당하는 라벨이 존재하지 않습니다.");
        }

        Labels label = labelDTO.convertToEntity();

        // DTO를 앤티티에 담아 JPA를 통해 등록
        Notes newNote = notesDTO.convertToEntity();

        newNote.setUserId(authUtil.getUserId());
        newNote.setNoteDate(LocalDateTime.now());
        newNote.setNoteStatus("Y");
        newNote.setLabels(label);

        notesRepository.save(newNote);

        return newNote;
    }


    /**
     * 매매일지를 삭제하는 메소드
     * */
    public void deleteNoteByNoteId(NotesDTO notesDTO) {

        // 실제로 삭제하는 것이 아니라 단지 상태값을 N으로 만드는 것
        // DTO를 앤티티에 담아 넘긴다.
        Notes deleteNote = notesDTO.convertToEntity();
        deleteNote.setNoteStatus("N");

        notesRepository.save(deleteNote);
    }

    /* 노트의 내용을 변경하는 메소드 */
    public void updateNoteByNoteId(NotesDTO notesDTO) {

        notesDTO.setNoteDate(LocalDateTime.now());
        notesRepository.save(notesDTO.convertToEntity());

    }
}
