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
    private String nomeProduto;
//    private String descricao;
//    private Float preco;
//    private NaoSei imagem;
//    private Boolean requerLote;
//    private Float peso;
//    private Boolean generico;

    private Long idCliente;
    private Float fidelidadePontosCliente;

    public static FeedbackDTO create(Feedback feedback) {
        ModelMapper modelMapper = new ModelMapper();
        FeedbackDTO dto = modelMapper.map(feedback, FeedbackDTO.class);

        dto.idProduto = feedback.getProduto().getId();
        dto.nomeProduto = feedback.getProduto().getNome();

        dto.idCliente = feedback.getCliente().getId();
        dto.fidelidadePontosCliente = feedback.getCliente().getFidelidadePontos();

        return dto;
    }
}
