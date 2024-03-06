package com.code2am.stocklog.domain.auth.common.controller;


import com.code2am.stocklog.domain.users.models.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("test/도착");

        return ResponseEntity.ok("success");
    }
}
