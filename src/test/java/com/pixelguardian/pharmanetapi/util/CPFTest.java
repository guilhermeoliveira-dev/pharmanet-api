package com.pixelguardian.pharmanetapi.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

//    @Test
//    void parseCaracteres() {
//    }

    @Test
    void validacaoMatematica_e_valido() {

        String cpf = "01234567890";
        Assertions.assertTrue(CPF.validacaoMatematica(cpf));

    }

    @Test
    void validacaoMatematica_e_invalido() {

        String cpf = "01234567891";
        Assertions.assertFalse(CPF.validacaoMatematica(cpf));

    }
}