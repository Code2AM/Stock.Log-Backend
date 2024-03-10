package com.code2am.stocklog.domain.users.models.entity;

import com.code2am.stocklog.domain.auth.common.enums.UserRole;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "TBL_USERS")
public class Users {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CAPITAL")
    private Integer capital;

    @Column(name = "SOCIAL")
    private String social;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createDate;

    // 유저 권한
    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    /* Builder */
    @Builder
    public Users(String email, String password, String status, Integer capital, String social, LocalDateTime createDate, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.status = status;
        this.capital = capital;
        this.social = social;
        this.createDate = createDate;
        this.userRole = UserRole.ROLE_USER;
    }

    /* Encode Password */
    public Users encodePassword(PasswordEncoder passwordEncoder){
        Users user = Users.builder()
                .email(this.email)
                .password(passwordEncoder.encode(this.password))
                .status(this.status)
                .capital(this.capital)
                .social(this.social)
                .createDate(this.createDate)
                .userRole(this.userRole)
                .build();

        System.out.println("빌더를 사용함 : ");
        System.out.println(user.password);
        return user;
    }

    /* DTO Converter */
    public UserDTO convertToDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(this.userId);
        userDTO.setEmail(this.email);
        userDTO.setPassword(this.password);
        userDTO.setStatus(this.status);
        userDTO.setSocial(this.social);
        userDTO.setCapital(this.capital);
        userDTO.setCreateDate(this.createDate);
        userDTO.setUserRole(this.userRole);

        return userDTO;
    }
}
