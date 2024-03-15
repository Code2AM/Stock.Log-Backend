package com.code2am.stocklog.domain.auth.common.util;

import com.code2am.stocklog.domain.auth.common.handler.exceptions.AuthUtilException;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthUtil {

    @Autowired
    UsersRepository usersRepository;

   private AuthUtil() { }


    /* authentication을 활용해서 userId (pk) 를 반환 */
    public Integer getUserId(){

        // 사용자의 ID를 얻는 방법
        Integer userId = usersRepository.findByEmail(getUserEmail()).get().getUserId();

        return userId;
    }


    /* authentication 에 있는 userEmail을 반환 */
    public String getUserEmail(){

        Authentication authentication;

        // authentication 에러를 핸들링하기 위한 try & catch
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }
        catch (IllegalStateException e){
            throw new AuthUtilException("Security Context 에 Authentication 객체가 없습니다. 혹은 Authentication 객체에 Principal 이 없습니다.");
        }
        catch (ClassCastException e){
            throw new AuthUtilException("Principal 이 UserDetails 타입이 아닙니다.");
        }
        catch (NoSuchFieldError  e){
            throw new AuthUtilException("UserDetails 객체에 userId 속성이 없습니다.");
        }

        // 사용자의 ID를 얻는 방법
        return authentication.getName();
    }

    /* 입력 받은 유저아이디가 유효한 아이디인지 확인하는 메소드 */

}
