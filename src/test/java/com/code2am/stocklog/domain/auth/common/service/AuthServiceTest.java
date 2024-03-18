package com.code2am.stocklog.domain.auth.common.service;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.util.AuthUtil;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.auth.jwt.repository.RefreshTokenRepository;
import com.code2am.stocklog.domain.auth.jwt.util.TokenUtils;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.security.auth.login.CredentialException;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
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

    @Spy
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);

    @Mock
    private AuthUtil authUtil;

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;



    void setUp(UserDTO loginUser) {
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        TokenDTO expectedToken = TokenDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .accessTokenExpiresIn(3600L)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@test.com",
                "password",
                Collections.singletonList(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return "ROLE_USER";
                    }
                })
        );

        // authenticationManagerBuilder.getObject()가 null이 아닌
        // authenticationManager 스파이 객체를 반환하도록 설정합니다.

        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);

        if (loginUser == userDTO){
            given(authenticationManagerBuilder.getObject().authenticate(any())).willReturn(authentication);
        }
        else {
                given(authenticationManagerBuilder.getObject().authenticate(any())).willThrow(new AuthUtilException("비번 혹은 아디디 달라요"));
        }



        given(tokenUtils.generateTokenDto(any())).willReturn(expectedToken);

       // given(Authentication.class.getName()).willReturn("abc123");
    }

    int setUp2(UserDTO loginUser, int result) {
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        TokenDTO expectedToken = TokenDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .accessTokenExpiresIn(3600L)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "test@test.com",
                "password",
                Collections.singletonList(new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return "ROLE_USER";
                    }
                })
        );

        // authenticationManagerBuilder.getObject()가 null이 아닌
        // authenticationManager 스파이 객체를 반환하도록 설정합니다.

        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);

        if (loginUser == userDTO){
            given(authenticationManagerBuilder.getObject().authenticate(any())).willReturn(authentication);
        }
        else {
            given(authenticationManagerBuilder.getObject().authenticate(any())).willReturn(authentication);
            result += 1;
        }

        given(tokenUtils.generateTokenDto(any())).willReturn(expectedToken);

        // given(Authentication.class.getName()).willReturn("abc123");

        return result;
    }



    /* signup */


    // 성공
    @Test
    public void signup_성공() throws Exception {
        // given
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        // stub
        given(usersRepository.existsByEmail(anyString())).willReturn(false);

        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        // when
        String result = authService.signup(userDTO);

        // then
        assertEquals("등록성공", result);
        verify(usersRepository).save(any());

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
        given(usersRepository.existsByEmail(anyString())).willReturn(true);

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

        TokenDTO expectedToken = TokenDTO.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .accessTokenExpiresIn(3600L)
                .build();

        setUp(userDTO);

        // stub

        // when
        TokenDTO actualToken = authService.login(userDTO);

        // then
        assertNotNull(actualToken);
        assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
        assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());
        assertEquals(expectedToken.getAccessTokenExpiresIn(), actualToken.getAccessTokenExpiresIn());
    }

    // 아이디가 다른 경우
    @Test
    public void login_아이디_없는_경우() {

        // given
        int result = 0;

        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        result = setUp2(userDTO, result);


        String message = "아이디 혹은 비밀번호가 다릅니다.";

        TokenDTO token = authService.login(userDTO);

        assertEquals(result, 1);

    }


    // 성공
    @Test
    void reissue_성공() {
        // given
        UserDTO userDTO = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        TokenDTO expectedToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();
    }

}