package com.code2am.stocklog.domain.auth.common.service;

import com.code2am.stocklog.domain.users.models.entity.Users;
import com.code2am.stocklog.domain.users.repository.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthDetailsServiceTest {

    @InjectMocks
    private AuthDetailsService authDetailsService;

    @Mock
    private UsersRepository usersRepository;

    // loadUserByUsername
    @Test
    public void loadUserByUsername_이메일이_있는_경우() {
        // Arrange
        Users user = new Users();
        user.setEmail("example@example.com");
        user.setPassword("password");

        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = authDetailsService.loadUserByUsername("example@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals("example@example.com", userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_유저없음() {

        // Arrange
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // assertThrows를 사용하여 예외 포착 및 예외 객체 반환
        UsernameNotFoundException thrown = assertThrows(
                UsernameNotFoundException.class,
                () -> authDetailsService.loadUserByUsername("unknown@example.com")
        );

        // 예외 메세지 확인
        assertEquals("흔히 하는 실수예요. 비밀번호나 아이디가 한 글자라도 휴가를 갔나봐요. 확인하고 다시 시도해봐요.", thrown.getMessage());
    }
}