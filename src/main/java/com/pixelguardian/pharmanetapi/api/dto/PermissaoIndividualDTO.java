package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.entity.PermissaoIndividual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.modelmapper.ModelMapper;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoIndividualDTO {
    Long id;
    Long cargoId;
    String cargoNome;
    Long permissaoId;
    String permissaoNome;

    public static PermissaoIndividualDTO create(PermissaoIndividual permissaoIndividual){

//        ModelMapper modelMapper = new ModelMapper();
//        PermissaoIndividualDTO dto = modelMapper.map(permissaoIndividual, PermissaoIndividualDTO.class);

        PermissaoIndividualDTO dto = new PermissaoIndividualDTO();

        dto.id = permissaoIndividual.getId();

        dto.cargoId = permissaoIndividual.getCargo().getId();
        dto.cargoNome = permissaoIndividual.getCargo().getNome();

        dto.permissaoId = permissaoIndividual.getPermissao().getId();
        dto.permissaoNome = permissaoIndividual.getPermissao().getNome();

        return dto;
    }

}