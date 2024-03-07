package com.code2am.stocklog.domain.common.utils;

import com.code2am.stocklog.domain.labels.models.dto.LabelsDTO;
import com.code2am.stocklog.domain.labels.models.entity.Labels;
import com.code2am.stocklog.domain.notes.models.dto.NotesDTO;
import com.code2am.stocklog.domain.notes.models.entity.Notes;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommonUtils {

    UsersRepository usersRepository;


    /* 사용자의 인증 정보를 기반으로 userNo 를 반환 */
    public Integer getUserId(){

        // 사용자의 ID를 얻는 방법
        Integer userId = usersRepository.findByEmail(getUserEmail()).get().getUserId();
        return userId;
    }


    /* 사용자의 인증 정보를 기반으로 userEmail 을 반환 */
    public String getUserEmail(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 ID를 얻는 방법
        String email = authentication.getName();
        return email;
    }


    /* Entity Converters */


    /* Entity to DTO */

    /* Users to UserDTO */
    public UserDTO convertToUserDTO(Users users){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(users.getUserId());
        userDTO.setEmail(users.getEmail());
        userDTO.setPassword(users.getPassword());
        userDTO.setStatus(users.getStatus());
        userDTO.setCapital(users.getCapital());
        userDTO.setSocial(users.getSocial());
        userDTO.setCreateDate(users.getCreateDate());
        userDTO.setUserRole(users.getUserRole());

        return userDTO;
    }

    /* Notes to NotesDTO */
    public NotesDTO convertToNotesDTO(Notes note){
        NotesDTO notesDTO = new NotesDTO();
        notesDTO.setNoteId(note.getNoteId());
        notesDTO.setNoteContents(note.getNoteContents());
        notesDTO.setNoteDate(note.getNoteDate());
        notesDTO.setNoteStatus(note.getNoteStatus());
        notesDTO.setUserId(note.getUserId());
        return notesDTO;
    }
    /* List<Notes> to List<NotesDTO> */
    public List<NotesDTO> convertToNotesDTOs(List<Notes> notes){
        List<NotesDTO> notesDTOS = new ArrayList<>();
        notes.forEach(note -> {
            notesDTOS.add(convertToNotesDTO(note));
        });
        return notesDTOS;
    }





    /* DTO to Entity */

    /* UserDTO to Users*/
    public Users convertToUser(UserDTO userDTO){
        Users user = new Users();
        user.setUserId(userDTO.getUserId());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setStatus(userDTO.getStatus());
        user.setCapital(userDTO.getCapital());
        user.setSocial(userDTO.getSocial());
        user.setCreateDate(userDTO.getCreateDate());
        user.setUserRole(userDTO.getUserRole());

        return user;
    }

    /* NotesDTO to Notes */
    public Notes convertToNote(NotesDTO notesDTO){
       Notes note = new Notes();
        note.setNoteId(notesDTO.getNoteId());
        note.setNoteContents(notesDTO.getNoteContents());
        note.setNoteDate(notesDTO.getNoteDate());
        note.setNoteStatus(notesDTO.getNoteStatus());
        note.setUserId(notesDTO.getUserId());
        return note;
    }
    /* List<NotesDTO> to List<Notes> */
    public List<Notes> convertToNotes(List<NotesDTO> notesDTOS){
        List<Notes> notes = new ArrayList<>();
        notesDTOS.forEach(notesDTO -> {
            notes.add(convertToNote(notesDTO));
        });
        return notes;
    }

    // LabelsDTO to Labels
    public Labels convertLabelsDtoToEntity(LabelsDTO labelsDTO) {
        Labels labels = new Labels();
        // DTO의 필드 값을 Entity에 설정
        labels.setLabelsId(labelsDTO.getLabelsId());
        labels.setLabelsTitle(labelsDTO.getLabelsTitle());
        labels.setLabelsStatus(labelsDTO.getLabelsStatus());
        labels.setUserId(labelsDTO.getUserId());
        return labels;
    }
}
