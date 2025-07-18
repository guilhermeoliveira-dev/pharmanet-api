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
    private String senha;

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

//        Para previnir falhas de segurança, já que o DTO tenta mandar a senha, mas a gente precisa da senha no
//        DTO para que a conta possa ser criada.
        dto.senha = "";

        dto.idEndereco = cliente.getEndereco().getId();
        dto.uf = cliente.getEndereco().getUf();
        dto.cidade = cliente.getEndereco().getCidade();
        dto.cep = cliente.getEndereco().getCep();
        dto.bairro = cliente.getEndereco().getBairro();
        dto.logradouro = cliente.getEndereco().getLogradouro();
        dto.numero = cliente.getEndereco().getNumero();
        dto.complemento = cliente.getEndereco().getComplemento();

        return dto;
    }
}