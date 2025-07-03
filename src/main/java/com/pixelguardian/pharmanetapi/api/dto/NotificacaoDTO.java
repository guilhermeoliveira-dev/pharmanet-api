package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Notificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {

    private Long id;

    private Long idUsuario;
//    private String nome;
//    private String email;
//    private String senha;
//    private String cpf;
//    private String telefone;
//    private String dataAdmissao;

    private String mensagem;
    private String dataEnvio;
    private String tipoNotificacao;

    public static NotificacaoDTO create(Notificacao notificacao) {
        ModelMapper modelMapper = new ModelMapper();
        NotificacaoDTO dto = modelMapper.map(notificacao, NotificacaoDTO.class);

        dto.idUsuario = notificacao.getUsuario().getId();
//        dto.nome = notificacao.getUsuario().getNome();
//        dto.email = notificacao.getUsuario().getEmail();
//        dto.senha = notificacao.getUsuario().getSenha();
//        dto.cpf = notificacao.getUsuario().getCpf();
//        dto.telefone = notificacao.getUsuario().getTelefone();
//        dto.dataAdmissao = notificacao.getUsuario().getDataAdmissao();

        return dto;
    }
}
