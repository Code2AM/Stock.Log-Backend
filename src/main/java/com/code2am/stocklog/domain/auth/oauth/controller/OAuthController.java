package com.code2am.stocklog.domain.auth.oauth.controller;

import com.code2am.stocklog.domain.auth.common.enums.AuthConstants;
import com.code2am.stocklog.domain.auth.jwt.model.dto.TokenDTO;
import com.code2am.stocklog.domain.auth.oauth.model.dto.OAuthRequest;
import com.code2am.stocklog.domain.auth.oauth.model.dto.OAuthToken;
import com.code2am.stocklog.domain.auth.oauth.service.OAuthService;
import com.code2am.stocklog.domain.auth.oauth.util.KakaoAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "외부 인증 API", description = "외부 인증을 관리하는 API")
public class OAuthController {

    private final KakaoAPI kakaoAPI;

    private final OAuthService oAuthService;


    @Operation(
            summary = "카카오 인증 요청",
            description = "카카오 계정을 이용해 로그인을 할 수 있도록 요청을 보냅니다."
    )
    @Parameter(name = "request", description = "회원의 인증을 요청하는 코드")
    @PostMapping("/kakao")
    public ResponseEntity<?> getKakaoAuthorizeCode(@RequestBody OAuthRequest request){

        String code = request.getCode();
        System.out.println(code);

        System.out.println(1);

        OAuthToken oAuthToken = kakaoAPI.getToken(code);

        System.out.println("발급받은 카카오 토큰 : "+oAuthToken);

        TokenDTO tokenDTO = oAuthService.kakaoLogin(oAuthToken);

        System.out.println("반환할 token : "+tokenDTO);

        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, AuthConstants.TOKEN_TYPE+ tokenDTO.getAccessToken())
                .body(tokenDTO);
    }
}
