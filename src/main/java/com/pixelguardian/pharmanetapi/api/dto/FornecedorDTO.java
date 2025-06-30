package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Fornecedor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {

    private Long id;
    private String nome;
    private String cnpj;
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

    public static FornecedorDTO create(Fornecedor fornecedor) {
        ModelMapper modelMapper = new ModelMapper();
        FornecedorDTO dto = modelMapper.map(fornecedor, FornecedorDTO.class);

        dto.idEndereco = fornecedor.getEndereco().getId();
        dto.uf = fornecedor.getEndereco().getUf();
        dto.cidade = fornecedor.getEndereco().getCidade();
        dto.cep = fornecedor.getEndereco().getCep();
        dto.bairro = fornecedor.getEndereco().getBairro();
        dto.logradouro = fornecedor.getEndereco().getLogradouro();
        dto.numero = fornecedor.getEndereco().getNumero();
        dto.complemento = fornecedor.getEndereco().getComplemento();

        return dto;
    }
}
