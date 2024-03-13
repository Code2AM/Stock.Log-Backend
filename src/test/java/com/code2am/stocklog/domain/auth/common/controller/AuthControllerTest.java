package com.code2am.stocklog.domain.auth.common.controller;


import com.code2am.stocklog.domain.auth.common.handler.exceptions.NoRefreshTokenException;
import com.code2am.stocklog.domain.auth.common.service.AuthService;
import com.code2am.stocklog.domain.auth.jwt.handler.exceptions.JwtException;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// 1. 성공 케이스
// 2. 입력 데이터 검증
// 3. 인증과 권한
// 4. 예외 상황과 에러 처리
// 5. 비즈니스 로직 검증
// 6. 경계 값 분석


@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper 주입

//    @BeforeEach
//    void objectMapperSetting() {
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//    }


    @Test
    public void signup_성공() throws Exception {

        UserDTO newUser = UserDTO.builder().
                email("test@test.com")
                .password("password")
                .build();

        String successMessage = "회원가입 성공";

        given(authService.signup(newUser)).willReturn(successMessage);

        String requestBody = objectMapper.writeValueAsString(newUser);

        // 실행 & 검증
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(successMessage));
    }

    @Test
    public void signup_중복_이메일() throws Exception {
        // 준비
        UserDTO newUser = UserDTO.builder().
                email("test@test.com")
                .password("password")
                .build();

        String errorMessage = "이미 사용 중인 이메일입니다.";

        given(authService.signup(newUser)).willThrow(new RuntimeException(errorMessage));

        String requestBody = objectMapper.writeValueAsString(newUser);

        // 실행 & 검증
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }


    // 올바르지 않은 이메일을 입력한 경우
    @Test
    void signup_잘못된_이메일_형식() throws Exception {

        // 준비
        UserDTO newUser = UserDTO.builder().
                email("testtest.com")
                .password("password")
                .build();

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효한 이메일 주소가 아닙니다."));
    }


    // 올바르지 않은 비밀번호를 입력한 경우
    @Test
    void 비밀번호_6자리_이하_테스트() throws Exception {

        // 준비
        UserDTO newUser = UserDTO.builder().
                email("test@test.com")
                .password("1234")
                .build();

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호는 최소 6자리 이상이여야 합니다."));
    }

    @Test
    void 비밀번호_공백_테스트() throws Exception {

        // 준비
        UserDTO newUser = UserDTO.builder().
                email("test@test.com")
                .password(null)
                .build();

        String requestBody = objectMapper.writeValueAsString(newUser);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호를 입력해주세요."));
    }


    /* /auth/login */

    // 성공 케이스
    @Test
    void login_성공() throws Exception {

        // 입력 받은 데이터
        UserDTO loginUser = UserDTO.builder()
                .email("test@test.com")
                .password("password")
                .build();

        // 반환받는 토큰
        // 지금 토큰을 바로 인증할 수 없기에 accessToken, refreshToken의 값을 abc123으로 통일
        TokenDTO newToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        given(authService.login(loginUser)).willReturn(newToken);

        // loginUser를 json로 parsing 해서 content에 반환
        String requestBody = objectMapper.writeValueAsString(loginUser);

        System.out.println("loginUser");
        System.out.println(loginUser);
        System.out.println("requestBody");
        System.out.println(requestBody);


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.accessTokenExpiresIn").exists())
                .andExpect(jsonPath("$.accessTokenExpiresIn").isNotEmpty());
    }

    // 아이디가 없는 경우
    @Test
    public void login_아이디_없음() throws Exception {
        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("test@test.com")
                .password("password")
                .build();

        String errorMessage = "흔히 하는 실수예요. 비밀번호나 아이디가 한 글자라도 휴가를 갔나봐요. 확인하고 다시 시도해봐요.";

        given(authService.signup(loginUser)).willThrow(new BadCredentialsException(errorMessage));

        String requestBody = objectMapper.writeValueAsString(loginUser);

        // 실행 & 검증
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }


    // 아이디는 있지만 비밀번호가 일치하지 않는 경우
    @Test
    public void login_비밀번호_불일치() throws Exception {
        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("test@test.com")
                .password("password")
                .build();

        String errorMessage = "흔히 하는 실수예요. 비밀번호나 아이디가 한 글자라도 휴가를 갔나봐요. 확인하고 다시 시도해봐요.";

        given(authService.signup(loginUser)).willThrow(new BadCredentialsException(errorMessage));

        String requestBody = objectMapper.writeValueAsString(loginUser);

        // 실행 & 검증
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(errorMessage));
    }


    // 입력한 아이디가 올바른 이메일 형식이 아닌 경우
    @Test
    void login_잘못된_이메일_형식() throws Exception {

        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("testtest.com")
                .password("password")
                .build();


        String requestBody = objectMapper.writeValueAsString(loginUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("유효한 이메일 주소가 아닙니다."));
    }

    // 올바르지 못한 형식의 비밀번호를 입력한 경우
    @Test
    void login_비밀번호_6자리_이하_테스트() throws Exception {

        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("test@test.com")
                .password("1234")
                .build();

        String requestBody = objectMapper.writeValueAsString(loginUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호는 최소 6자리 이상이여야 합니다."));
    }

    @Test
    void login_비밀번호_공백_테스트() throws Exception {

        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("test@test.com")
                .password(null)
                .build();

        TokenDTO newToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        given(authService.login(loginUser)).willReturn(newToken);

        String requestBody = objectMapper.writeValueAsString(loginUser);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호를 입력해주세요."));
    }





    /* /auth/reissue */


    // 성공
    @Test
    void reissue_성공() throws Exception {

        // 전달받은 토큰
        TokenDTO deliveredRefreshToken = TokenDTO.builder()
                .refreshToken("abc123")
                .build();

        // 반환활 토큰
        TokenDTO returnedRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        given(authService.reissue(deliveredRefreshToken)).willReturn(returnedRefreshToken);

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(deliveredRefreshToken);

        System.out.println("deliveredRefreshToken");
        System.out.println(deliveredRefreshToken);
        System.out.println("requestBody");
        System.out.println(requestBody);


        mockMvc.perform(post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.accessTokenExpiresIn").exists())
                .andExpect(jsonPath("$.accessTokenExpiresIn").isNotEmpty());
    }

    // 입력받은 refreshToken이 유효하지 않는 경우
    @Test
    void reissue_유효하지_않은_토큰() throws Exception {

        // 전달받은 토큰
        TokenDTO deliveredRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        String errorMessage = "Refresh Token 이 유효하지 않습니다.";

        given(authService.reissue(deliveredRefreshToken)).willThrow(new JwtException(errorMessage));

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(deliveredRefreshToken);


        mockMvc.perform(post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(errorMessage));
    }

    // Refresh Token 일치하지 않는 경우
    @Test
    void reissue_유저정보_일치하지_않음() throws Exception {

        // 전달받은 토큰
        TokenDTO deliveredRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        String errorMessage = "토큰의 유저 정보가 일치하지 않습니다.";

        given(authService.reissue(deliveredRefreshToken)).willThrow(new JwtException(errorMessage));

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(deliveredRefreshToken);


        mockMvc.perform(post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(errorMessage));
    }

    // refreshToken이 DB에 존재하지 않는 경우 (이미 logout 된  사용자인 경우)
    @Test
    void reissue_로그아웃한_사용자() throws Exception {

        // 전달받은 토큰
        TokenDTO deliveredRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .accessTokenExpiresIn(123L)
                .build();

        String errorMessage = "로그아웃 된 사용자입니다.";

        given(authService.reissue(deliveredRefreshToken)).willThrow(new JwtException(errorMessage));

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(deliveredRefreshToken);


        mockMvc.perform(post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(errorMessage));
    }



    /* auth/logout */


    // 성공
    @Test
    void logout_성공() throws Exception {

        // 전달받은 토큰
        TokenDTO logoutRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .build();


        String message = "성공적으로 로그아웃 되었습니다.";


        given(authService.logout(logoutRefreshToken)).willReturn(message);

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(logoutRefreshToken);


        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // DB에 refreshToken이 없는 경우 (이미 로그아웃 된 경우)
    @Test
    void logout_이미_로그아웃() throws Exception {

        // 전달받은 토큰
        TokenDTO logoutRefreshToken = TokenDTO.builder()
                .accessToken("abc123")
                .refreshToken("abc123")
                .build();


        String message = "이미 로그아웃된 사용자입니다.";


        given(authService.logout(logoutRefreshToken)).willThrow(new NoRefreshTokenException(message));

        // Front 에서 전달할 token
        String requestBody = objectMapper.writeValueAsString(logoutRefreshToken);


        mockMvc.perform(post("/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(message));
    }


    /* auth/changePassword */


    // 성공
    @Test
    void changePassword_성공() throws Exception {

        // 준비
        UserDTO loginUser = UserDTO.builder().
                email("test@test.com")
                .password("123456")
                .build();

        String message = "비밀번호 변경 성공!";

        given(authService.changePassword(loginUser)).willReturn(message);

        String requestBody = objectMapper.writeValueAsString(loginUser);

        mockMvc.perform(post("/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string(message));
    }

    // 입력 받은 비밀번호가 비밀번호 형식에 맞지 않는 경우
    @Test
    void changePassword_비밀번호_6자리_이하_테스트() throws Exception {

        UserDTO changePassUser = UserDTO.builder().
                email("test@test.com")
                .password("12345")
                .build();

        String requestBody = objectMapper.writeValueAsString(changePassUser);

        mockMvc.perform(post("/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호는 최소 6자리 이상이여야 합니다."));
    }

    @Test
    void changePassword_비밀번호_공백_테스트() throws Exception {

        // 준비
        UserDTO changePassUser = UserDTO.builder().
                email("test@test.com")
                .password(null)
                .build();

        String requestBody = objectMapper.writeValueAsString(changePassUser);

        mockMvc.perform(post("/auth/changePassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("비밀번호를 입력해주세요."));
    }


}