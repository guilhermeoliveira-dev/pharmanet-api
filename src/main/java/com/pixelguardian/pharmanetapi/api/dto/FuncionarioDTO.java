package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Funcionario;
import com.pixelguardian.pharmanetapi.model.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String dataAdmissao;
    //private NaoSei imagemPerfil;

    private Float salario;
    private String expediente;

    private Long idEndereco;
    private String uf;
    private String cidade;
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;

    private Long idCargo;
    private String nomeCargo;

    private Long idFarmacia;
    private String nomeFarmacia;

    public static FuncionarioDTO create(Funcionario funcionario) {
        ModelMapper modelMapper = new ModelMapper();
        FuncionarioDTO dto = modelMapper.map(funcionario, FuncionarioDTO.class);

        // Para previnir falhas de segurança, já que o DTO tenta mandar a senha, mas a gente precisa da senha no
        // DTO para que a conta possa ser criada.
        dto.senha = "";

        dto.salario = funcionario.getSalario();
        dto.expediente = funcionario.getExpediente();

        dto.idEndereco = funcionario.getEndereco().getId();
        dto.logradouro = funcionario.getEndereco().getLogradouro();
        dto.numero = funcionario.getEndereco().getNumero();
        dto.complemento = funcionario.getEndereco().getComplemento();
        dto.bairro = funcionario.getEndereco().getBairro();
        dto.cidade = funcionario.getEndereco().getCidade();
        dto.uf = funcionario.getEndereco().getUf();
        dto.cep = funcionario.getEndereco().getCep();

        dto.idCargo = funcionario.getCargo().getId();
        dto.nomeCargo = funcionario.getCargo().getNome();

        dto.idFarmacia = funcionario.getFarmacia().getId();
        dto.nomeFarmacia = funcionario.getFarmacia().getNome();
        return dto;
    }
}