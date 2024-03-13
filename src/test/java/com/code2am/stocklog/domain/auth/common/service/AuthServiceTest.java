package com.code2am.stocklog.domain.auth.common.service;

import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.auth.jwt.repository.RefreshTokenRepository;
import com.code2am.stocklog.domain.auth.jwt.util.TokenUtils;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


// 1. 성공 케이스
// 2. 입력 데이터 검증
// 3. 인증과 권한
// 4. 예외 상황과 에러 처리
// 5. 비즈니스 로직 검증
// 6. 경계 값 분석

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthUtil authUtil;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

//    @Spy
//    private PasswordEncoder passwordEncoder;


    /* signup */


    // 성공
    @Test
    public void signup_성공() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        // DB에 email이 없는 상황을 가정
        when(usersRepository.existsByEmail(anyString())).thenReturn(false);


        // when
        String result = authService.signup(userDTO);

        // then
        assertEquals("사용자 등록이 성공하지 않았습니다.", result);

    }

    // 가입하려는 이메일이 중복되는 경우
    @Test
    public void signup_이메일_중복() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        // DB에 email이 있는 상황을 가정
        when(usersRepository.existsByEmail(anyString())).thenReturn(true);

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> authService.signup(userDTO)
        );

        // 예외 메세지 확인
        assertEquals("이미 가입되어 있는 이메일입니다", thrown.getMessage());
    }




    /* login */


    // 성공
    @Test
    public void login_성공() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        usersRepository.save(userDTO.convertToEntity());

        TokenDTO expectedToken  = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        // MOCK : DB에 email이 없는 상황을 가정
        when(authService.login(userDTO)).thenReturn(expectedToken);

        // when
        TokenDTO actualToken = authService.login(userDTO);

        // Then (Verification)
        assertNotNull(actualToken);
        assertEquals(expectedToken, actualToken, "Returned token should match expected token");

    }

// 아이디가 다른 경우

// 비밀번호가 다른 경우


    /* reissue */
//    @Test
//    void reissue() {
//    }

// 성공
    @Test
    void reissue_성공(){
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        TokenDTO expectedToken  = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();
    }

// rt가 유효하지 않는 경우

// rt가 db에 없는 경우 -> 사용자가 이미 로그아웃 한 경우

// 토큰의 유저 정보가 유효하지 않는 경우


    /* logout */
//    @Test
//    void logout() {
//    }

// 성공

// db에 rt가 없는 경우 -> 이미 로그아웃한 사용자


    /* changePassword */
//    @Test
//    void changePassword() {
//    }

// 성공

// 사용자의 이메일이 없는 경우

// 입력한 비밀번호가 이전 비밀번호와 같은 경우

//}

}