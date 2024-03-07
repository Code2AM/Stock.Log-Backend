package com.code2am.stocklog.domain.auth.common.util;

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


    public Integer getUserId(){

        // 사용자의 ID를 얻는 방법
        Integer userId = usersRepository.findByEmail(getUserEmail()).get().getUserId();

        return userId;
    }

    public String getUserEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자의 ID를 얻는 방법
        String email = authentication.getName();
        return email;
    }
}
