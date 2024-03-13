package com.code2am.stocklog.domain.auth.email.util;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;

class EmailUtilsTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailUtils emailUtils;

    @Test
    public void sendEmail_성공() throws MessagingException {
        // Given (Test Setup)
        String from = "test@example.com";
        String to = "receiver@example.com";
        String title = "테스트 제목";
        String content = "테스트 내용";

        // When (Test Execution)
        emailUtils.sendEmail(from, to, title, content);

        // Then (Verification)
        // Verify that `mailSender.send` was called with the expected message
        Mockito.verify(mailSender).send(Mockito.any(MimeMessage.class));

        // Verify that the MimeMessageHelper was configured correctly
        ArgumentCaptor<MimeMessageHelper> helperCaptor = ArgumentCaptor.forClass(MimeMessageHelper.class);
        Mockito.verify(mailSender).createMimeMessage();
        Mockito.verify(new MimeMessageHelper(Mockito.any(MimeMessage.class), Mockito.anyBoolean(), Mockito.anyString())).setFrom(from);
        Mockito.verify(new MimeMessageHelper(Mockito.any(MimeMessage.class), Mockito.anyBoolean(), Mockito.anyString())).setTo(to);
        Mockito.verify(new MimeMessageHelper(Mockito.any(MimeMessage.class), Mockito.anyBoolean(), Mockito.anyString())).setSubject(title);
        Mockito.verify(new MimeMessageHelper(Mockito.any(MimeMessage.class), Mockito.anyBoolean(), Mockito.anyString())).setText(content, true);
    }

    @Test
    public void sendEmail_예외발생() throws MessagingException {
        // Given (Test Setup)
        String from = "test@example.com";
        String to = "receiver@example.com";
        String title = "테스트 제목";
        String content = "테스트 내용";

        // When (Test Execution)
        // Mocking 설정을 변경하여 MessagingException 발생 시뮬레이션
        doThrow(MessagingException.class).when(mailSender).send(Mockito.any(MimeMessage.class));

        emailUtils.sendEmail(from, to, title, content);

        // Then (Verification)
        // MessagingException 예외 발생 확인
    }
}