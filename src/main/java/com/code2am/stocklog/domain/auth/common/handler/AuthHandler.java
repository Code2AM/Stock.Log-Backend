package com.code2am.stocklog.domain.auth.common.handler;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.auth.common.handler.exceptions.NoRefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthHandler {

    // 아이디 혹은 비밀번호 잘못 입력한 경우
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401 상태 코드 설정
    @ResponseBody
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException e) {

        String message = "흔히 하는 실수예요. 비밀번호나 아이디가 한 글자라도 휴가를 갔나봐요. 확인하고 다시 시도해봐요.";

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    // 이미 로그아웃된 경우
    @ExceptionHandler(NoRefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleNoRefreshTokenException(NoRefreshTokenException e) {

        System.out.println("AuthHandler");

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }

    // AuthUtil 관련 에러 핸들링 - authentication 관련 에러들
    @ExceptionHandler(AuthUtilException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<String> handleAuthUtilException(AuthUtilException e) {

        System.out.println("AuthHandler");

        String message = e.getMessage();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(message);
    }


}
