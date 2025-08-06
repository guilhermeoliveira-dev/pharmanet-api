package com.pixelguardian.pharmanetapi.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class RandomNumberGenerator {


    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static RandomNumberGenerator rng;

    private SecureRandom secureRng;

    private RandomNumberGenerator(){
        try {
            secureRng = SecureRandom.getInstance("NativePRNG");
        } catch (NoSuchAlgorithmException e) {
            try {
                secureRng = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static RandomNumberGenerator getRng (){
        if (rng == null){
            rng = new RandomNumberGenerator();
        }
        return rng;
    }

    private String gerarAlphaNumericoNDigitos(int n){
        if (n <= 0){
            return "";
        }

        char[] caracteres = new char[n];

        for (int i = 0; i < n; i++){
            caracteres[i] = ALPHANUMERIC_CHARS.charAt(secureRng.nextInt(ALPHANUMERIC_CHARS.length()));
        }

        return Arrays.toString(caracteres);
    }

    public String gerarAlphaNumericoSeisDigitos(){

        return gerarAlphaNumericoNDigitos(6);
    }

}
