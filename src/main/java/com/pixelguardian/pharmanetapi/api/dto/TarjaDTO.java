package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Tarja;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarjaDTO implements Serializable {
    Long id;
    String nome;
    String cor;
    Boolean requerReceita;
    Boolean retemReceita;

    public static TarjaDTO create(Tarja tarja) {
        ModelMapper modelMapper = new ModelMapper();
        TarjaDTO dto = modelMapper.map(tarja, TarjaDTO.class);
        return dto;
    }
}