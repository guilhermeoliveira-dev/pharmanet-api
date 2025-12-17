package com.pixelguardian.pharmanetapi.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void post() {
        String senhaCriptografada = passwordEncoder.encode("1dasilva");
        System.out.println(senhaCriptografada);
    }
}
