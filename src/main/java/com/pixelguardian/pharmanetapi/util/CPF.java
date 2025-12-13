package com.pixelguardian.pharmanetapi.util;

import com.pixelguardian.pharmanetapi.exception.StringEstruturadaInvalidaException;

public record CPF(String cpf) implements StringEstruturada {

    public CPF(String cpf){
        this.cpf = cpf;
        if (!validarValor()){
            throw new StringEstruturadaInvalidaException("O CPF \""+ cpf +"\" é inválido.");
        }
    }

    @Override
    public boolean validarValor() {

        if (cpf.length() == 0){
            return false;
        }

        return true;
    }

    @Override
    public String parseCaracteres(String valor) {
        return "";
    }

    @Override
    public String getString() {
        return cpf.substring(0, 3) + "." +cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }

    public static boolean validacaoMatematica(String valor){

        int[] digitos = new int[11];

        char[] chars = valor.toCharArray();

        for (int i = 0; i < 11; i++){
            char b = chars[i];
            if (!Character.isDigit(b)){
                return false;
            }
            digitos[i] = Character.getNumericValue(b);
        }

        int somatorio1 = 0;

        for (int i = 0, peso = 10; i < 9; i++, peso--){
            somatorio1 += digitos[i] * peso;
        }

        int resto1 =  somatorio1 % 11;

        int dv1;

        if (resto1 < 2){
            dv1 = 0;
        }
        else{
            dv1 = 11 - resto1;
        }

        if (dv1 != digitos[9]){
            return false;
        }

        int somatorio2 = 0;

        for (int i = 0, peso = 11; i < 10; i++, peso--){
            somatorio2 += digitos[i] * peso;
        }

        int resto2 = somatorio2 % 11;

        int dv2;

        if (resto2 < 2){
            dv2 = 0;
        }
        else{
            dv2 = 11 - resto2;
        }

        return dv2 == digitos[10];
    }
}
