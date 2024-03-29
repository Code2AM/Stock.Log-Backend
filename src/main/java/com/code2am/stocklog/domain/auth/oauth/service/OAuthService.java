package com.code2am.stocklog.domain.auth.oauth.service;

import com.code2am.stocklog.domain.auth.common.enums.UserRole;
import com.code2am.stocklog.domain.auth.common.service.AuthService;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.auth.oauth.model.dto.OAuthToken;
import com.code2am.stocklog.domain.auth.oauth.model.profile.KakaoProfile;
import com.code2am.stocklog.domain.auth.oauth.util.KakaoAPI;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final KakaoAPI kakaoAPI;
    private final UsersRepository usersRepository;
    private final AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public TokenDTO kakaoLogin(OAuthToken token) {

        //사용자의 정보를 불어온다
        KakaoProfile profile = kakaoAPI.getMemberInfo(token.getAccessToken());

        // 사용자의 email 이 DB에 등록 되어 있는지 확인, 없다면 회원가입
        String email = profile.getKakao_account().getEmail();

        System.out.println("사용자의 이메일: " + email);

        // DB 조회 결과 없다면
        if (!(usersRepository.existsByEmail(email))) {
            System.out.println("이메일 조회 결과 없음, 등록 시작");

            //카카오 유저 만듬
            UserDTO newKakaoUser = UserDTO.builder()
                    .email(email)
                    .password("123123")
                    .social("KAKAO")
                    .userRole(UserRole.ROLE_USER)
                    .build();

            // 신규 유저 회원 가입
            authService.signup(newKakaoUser);

            // 등록한 유저로 로그인
            TokenDTO tokenDTO = authService.login(newKakaoUser);

            System.out.println("id 없을 때 반환되는 token");
            System.out.println(tokenDTO);

            return tokenDTO;

        } else {
            System.out.println("조회 결과 있음");
            Users kakaoUser = usersRepository.findByEmail(email).get();
            System.out.println(kakaoUser);

            // 인증 객체 통과를 위해 임시 비밀번호 설정
            UserDTO userDTO = UserDTO.builder().email(kakaoUser.getEmail()).password("123123").build();

            System.out.println("Ready for War");
            System.out.println(userDTO);

            TokenDTO tokenDTO = authService.login(userDTO);

            System.out.println("id 있을 때 반환되는 token");
            System.out.println(tokenDTO);
            return tokenDTO;

        }

    }

}
