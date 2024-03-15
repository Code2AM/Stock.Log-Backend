package com.code2am.stocklog.domain.auth.common.service;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.NoRefreshTokenException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.auth.jwt.handler.exceptions.JwtException;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.auth.jwt.model.entity.RefreshToken;
import com.code2am.stocklog.domain.auth.jwt.repository.RefreshTokenRepository;
import com.code2am.stocklog.domain.auth.jwt.util.TokenUtils;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UsersRepository usersRepository;
    private final AuthUtil authUtil;
    private final TokenUtils tokenUtils;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String signup(UserDTO userDTO) {

        // 받은 정보를 통해서 유저가 있는지 확인
        if (usersRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 이메일입니다");
        }

//        String password = passwordEncoder.encode(userDTO.getPassword());
//        userDTO.setPassword(password);

        // 유저를 등록 시킨다
        usersRepository.save(userDTO.convertToEntity().encodePassword(passwordEncoder));
        return "등록성공";
    }


    public TokenDTO login(UserDTO userDTO) {


        System.out.println(1);
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userDTO.toAuthentication(); // 추가함

        System.out.println(2);
        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 AuthDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        System.out.println(3);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDto = tokenUtils.generateTokenDto(authentication);

        System.out.println(4);
        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        System.out.println(5);
        // 5. 토큰 발급
        return tokenDto;
    }


    public TokenDTO reissue(TokenDTO tokenDTO) {

        // 1. Refresh Token 검증
        if (!tokenUtils.validateToken(tokenDTO.getRefreshToken())) {
            throw new JwtException("Refresh Token 이 유효하지 않습니다.");
        }
        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenUtils.getAuthentication(tokenDTO.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new JwtException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenDTO.getRefreshToken())) {
            throw new JwtException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDTO tokenDto = tokenUtils.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

    public String logout(TokenDTO tokenDTO) {
        System.out.println("logoutService");
        System.out.println(tokenDTO);

        // 저장소에서 tokenValue 를 기반으로 Refresh Token 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByValue(tokenDTO.returnRefreshTokenValue());
        System.out.println("Result :");
        System.out.println(refreshToken);

        // refreshToken이 null인 경우 이미 로그아웃된 경우임으로 NoRefreshTokenException을 반환
        if (refreshToken == null) {
            throw new NoRefreshTokenException("이미 로그아웃된 사용자입니다.");
        }


        System.out.println("refreshToken 있음 삭제 진행");
        // 5. Refresh Token 삭제
        refreshTokenRepository.delete(refreshToken);

        return "성공적으로 로그아웃 되었습니다.";
    }

    /* 사용자의 비밀번호를 바꾸는 메소드 */
    public String changePassword(UserDTO userDTO) {


        String email = userDTO.getEmail();
        Users user = usersRepository.findByEmail(email).get();

        System.out.println("이메일로 찾은 유저");
        System.out.println(user);

        // 비밀번호를 encode해서 입력
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        usersRepository.save(user);

        System.out.println("저장후 user: ");
        System.out.println(user);

        return "비밀번호 변경 성공!";

    }

}
