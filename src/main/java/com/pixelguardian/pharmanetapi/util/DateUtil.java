package com.pixelguardian.pharmanetapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final DateTimeFormatter formatoColadoReverso = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter formatoBarraReverso = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter formatoHifenReverso = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter formatoColado = DateTimeFormatter.ofPattern("ddMMyyyy");
    private static final DateTimeFormatter formatoBarra = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter formatoHifen = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String formatarColadoReverso(LocalDate data){
        if (data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoColadoReverso);
    }

    public static String formatarBarraReverso(LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoBarraReverso);
    }

    public static String formatarHifenReverso(LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoHifenReverso);
    }

    public static String formatarColado (LocalDate data){
        if (data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoColado );
    }

    public static String formatarBarra (LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoBarra );
    }

    public static String formatarHifen (LocalDate data){
        if(data == null)
            throw new IllegalArgumentException("Objeto data nulo");
        return data.format(formatoHifen );
    }

    public static String formatarISO (LocalDate data){
        return formatarHifenReverso(data);
    }

    public static LocalDate converterISO(String data){
        return LocalDate.parse(data, formatoHifenReverso);
    }

}
