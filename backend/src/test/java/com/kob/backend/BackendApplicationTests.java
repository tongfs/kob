package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("tfs"));
        System.out.println(passwordEncoder.matches("tfs", "$2a$10$ZH5ciCES12erjMLoFvIfgu7otyT6/k5jbHfQA85AdHoyGxpwZGRk6"));
    }
}
