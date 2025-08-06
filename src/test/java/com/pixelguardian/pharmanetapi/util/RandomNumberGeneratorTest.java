package com.pixelguardian.pharmanetapi.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class RandomNumberGeneratorTest {


    @Test
    void gerarCodigosDiferentes(){

        int n = 10000;

        RandomNumberGenerator rng = RandomNumberGenerator.getRng();

        String[] codigos = new String[n];

        for(int i = 0; i < n; i++){

            codigos[i] = rng.gerarAlphaNumericoSeisDigitos();

            System.out.println(codigos[i]);

            for (int j = 0; j < n; j++){

                if (i == j || codigos[j] != null || codigos[i] == null){
                    continue;
                }

                Assertions.assertNotEquals(codigos[j], codigos[i]);

            }
        }

    }

}
