package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Cliente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private String dataAdmissao;

    private Long idEndereco;
    private String uf;
    private String cidade;
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;

    private Float fidelidadePontos;

    public static ClienteDTO create(Cliente cliente) {
        ModelMapper modelMapper = new ModelMapper();
        ClienteDTO dto = modelMapper.map(cliente, ClienteDTO.class);
        dto.idEndereco = cliente.getEndereco().getId();
        dto.logradouro = cliente.getEndereco().getLogradouro();
        dto.numero = cliente.getEndereco().getNumero();
        dto.complemento = cliente.getEndereco().getComplemento();
        dto.bairro = cliente.getEndereco().getBairro();
        dto.cidade = cliente.getEndereco().getCidade();
        dto.uf = cliente.getEndereco().getUf();
        dto.cep = cliente.getEndereco().getCep();
        return dto;
    }
}