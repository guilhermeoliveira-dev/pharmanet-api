package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Feedback;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {

    private Long id;
    private String comentario;
    private Float nota;

    private Long idProduto;
    private String nome;
//    private String descricao;
//    private Float preco;
//    private NaoSei imagem;
//    private Boolean requerLote;
//    private Float peso;
//    private Boolean generico;

    private Long idCliente;
    private Float fidelidadePontos;

    public static FeedbackDTO create(Feedback feedback) {
        ModelMapper modelMapper = new ModelMapper();
        FeedbackDTO dto = modelMapper.map(feedback, FeedbackDTO.class);

        dto.idProduto = feedback.getProduto().getId();
        dto.nome = feedback.getProduto().getNome();

        dto.idCliente = feedback.getCliente().getId();
        dto.fidelidadePontos = feedback.getCliente().getFidelidadePontos();

        return dto;
    }
}
