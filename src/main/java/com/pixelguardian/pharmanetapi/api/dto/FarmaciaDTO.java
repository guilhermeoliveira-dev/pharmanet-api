package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Farmacia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmaciaDTO {

    private Long id;
    private String cnpj;
    private String nome;
    private String email;
    private String telefone;

    private Long idEndereco;
    private String uf;
    private String cidade;
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;

    public static FarmaciaDTO create(Farmacia farmacia) {
        ModelMapper modelMapper = new ModelMapper();
        FarmaciaDTO dto = modelMapper.map(farmacia, FarmaciaDTO.class);
        dto.idEndereco = farmacia.getEndereco().getId();
        dto.logradouro = farmacia.getEndereco().getLogradouro();
        dto.numero = farmacia.getEndereco().getNumero();
        dto.complemento = farmacia.getEndereco().getComplemento();
        dto.bairro = farmacia.getEndereco().getBairro();
        dto.cidade = farmacia.getEndereco().getCidade();
        dto.uf = farmacia.getEndereco().getUf();
        dto.cep = farmacia.getEndereco().getCep();
        return dto;
    }
}