package com.code2am.stocklog.domain.auth.common.controller;

import com.code2am.stocklog.domain.auth.common.enums.AuthConstants;
import com.code2am.stocklog.domain.auth.common.service.AuthService;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Tag(name = "Auth API", description = "인증, 유저정보 관련 API")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Operation(
            summary = "회원가입",
            description = "입력값(email, password)을 받아 회원가입을 진행시킵니다",
            tags = {"POST", "AuthController"}
    )
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserDTO newUser) {

        // 중복되는 email이 없는 경우
        try {
            String message = authService.signup(newUser);
            return ResponseEntity.ok(message);
        }

        // 중복된 email이 있는 경우
        catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(
            summary = "로그인",
            description = "입력값(email, password)을 받아 로그인을 진행시킵니다." +
                    "로그인 성공시, accessToken, refreshToken, expireDate를 반환시킨다",
            tags = {"POST"}
    )
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@Valid @RequestBody UserDTO memberRequestDto) {
        TokenDTO tokenDto = authService.login(memberRequestDto);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.TOKEN_TYPE + tokenDto.getAccessToken())
                .body(tokenDto);
    }

    @Operation(
            summary = "리프레스 토큰 재발급",
            description = "사용자에게 refreshToken 을 받아, DB에 있는 경우 accessToken 을 재발급 해줍니다.",
            tags = {"POST", "AuthController"}
    )
    @PostMapping("/reissue")
    public ResponseEntity<TokenDTO> reissue(@RequestBody TokenDTO tokenDTO) {
        return ResponseEntity.ok(authService.reissue(tokenDTO));
    }

    @Operation(
            summary = "로그아웃",
            description = "사용자에게 refreshToken 을 받아, DB에 있는 경우 refreshToken 을 삭제합니다.",
            tags = {"POST", "AuthController"}
    )
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody TokenDTO tokenDTO) {

        return ResponseEntity.ok(authService.logout(tokenDTO));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "사용자에게 UserDTO를 받아, DB에 있는 경우 user 정보를 update 합니다.",
            tags = {"POST", "AuthController"}
    )
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@Valid @RequestBody UserDTO userDTO) {

        return ResponseEntity.ok(authService.changePassword(userDTO));
    }

}
