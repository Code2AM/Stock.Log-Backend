package com.code2am.stocklog.domain.common.utils;

import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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
    public UserDTO convertToUsers(Users users){
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





    /* DTO to Entity */

    /* UserDTO to Users*/
    public Users convertToUserDTO(UserDTO userDTO){
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
}
