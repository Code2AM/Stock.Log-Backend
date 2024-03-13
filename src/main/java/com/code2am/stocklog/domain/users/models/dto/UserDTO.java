package com.code2am.stocklog.domain.users.models.dto;

import com.code2am.stocklog.domain.auth.common.enums.UserRole;
import com.code2am.stocklog.domain.users.models.entity.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.management.relation.Role;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UserDTO {
    private Integer userId;

    @Email(message = "유효한 이메일 주소가 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    // 비밀번호는 최소 6자리 이상
    @Size(min = 6, message = "비밀번호는 최소 6자리 이상이여야 합니다.")
    private String password;

    private String status;

    private Integer capital;

    private String social;

    private LocalDateTime createDate;

    private UserRole userRole;

    //생성자
    public UserDTO(Integer userId, String email, String password, String status, Integer capital, String social, LocalDateTime createDate, UserRole userRole) {
    }

    /* Builder */
    @Builder
    public UserDTO(String email, String password, String status, Integer capital, String social, LocalDateTime createDate, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.capital = capital;
        this.social = social;
        this.createDate = createDate;
        this.userRole = UserRole.ROLE_USER;
    }

    /* Encode Password */
    public UserDTO encodePassword(PasswordEncoder passwordEncoder){
        UserDTO userDTO = UserDTO.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .status(this.status)
                .capital(this.capital)
                .social(this.social)
                .createDate(this.createDate)
                .userRole(this.userRole)
                .build();
        return userDTO;
    }

    /* UserDTO를 인증토큰으로 반환하는 메소드 */
    public UsernamePasswordAuthenticationToken toAuthentication(){
        List<GrantedAuthority> authority = Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_USER.toString()));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                this.email,
                this.password,
                authority);

        return usernamePasswordAuthenticationToken;
    }


    /* KAKAO USERS */
//    public static UserDTO newKakaoUser(String email){
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail(email);
//        userDTO.setPassword("123");
//        userDTO.setUserRole(UserRole.ROLE_USER);
//        userDTO.setSocial("KAKAO");
//
//        return userDTO;
//
//    }

    /* Entity Converter */
    public Users convertToEntity(){
        Users user = new Users();
        user.setUserId(this.userId);
        user.setEmail(this.email);
        user.setPassword(this.password);
        user.setStatus(this.status);
        user.setSocial(this.social);
        user.setCapital(this.capital);
        user.setCreateDate(this.createDate);
        user.setUserRole(this.userRole);

        return user;
    }



}

