package com.pixelguardian.pharmanetapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatter {

    private static final DateTimeFormatter formatoColado = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter formatoBarra = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatoHifen = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatarData(LocalDate data){
        if (data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoColado);
    }

    public static String formatarDataBarra(LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoBarra);
    }

    public static String formatarDataHifen(LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoHifen);
    }

}
