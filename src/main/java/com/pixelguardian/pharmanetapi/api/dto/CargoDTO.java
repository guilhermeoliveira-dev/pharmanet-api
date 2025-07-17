package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.entity.PermissaoIndividual;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoDTO {

    private Long id;
    private String nome;
    private PermissaoIndividualDTO[] permissoes;

    public static CargoDTO create(Cargo cargo) {
        ModelMapper modelMapper = new ModelMapper();
        CargoDTO dto = modelMapper.map(cargo, CargoDTO.class);

        ArrayList<PermissaoIndividualDTO> permissoesList = new ArrayList<>();

        List<PermissaoIndividual> permissoesAtuais = cargo.getPermissoes();

        for (int i = 0; i < permissoesAtuais.size(); i++){
            if (permissoesAtuais.get(i).getTemPermissao()){
                permissoesList.add(PermissaoIndividualDTO.create(permissoesAtuais.get(i)));
            }
        }

        dto.permissoes = permissoesList.toArray(new PermissaoIndividualDTO[0]);

        return dto;
    }
}
