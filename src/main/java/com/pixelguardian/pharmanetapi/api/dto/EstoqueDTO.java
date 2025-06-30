package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Estoque;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {

    private Long id;
    private Integer quantidade;

    private Long idProduto;
    private String nomeProduto;
//    private String descricao;
//    private Float preco;
//    private NaoSei imagem;
//    private Boolean requerLote;
//    private Float peso;
//    private Boolean generico;

    private Long idFornecedor;
    private String nomeFornecedor;
    private String cnpj;
    private String email;
    private String telefone;

    public static EstoqueDTO create(Estoque estoque) {
        ModelMapper modelMapper = new ModelMapper();
        EstoqueDTO dto = modelMapper.map(estoque, EstoqueDTO.class);

        dto.idProduto = estoque.getProduto().getId();
        dto.nomeProduto = estoque.getProduto().getNome();

        dto.idFornecedor = estoque.getFornecedor().getId();
        dto.nomeFornecedor = estoque.getFornecedor().getNome();
        dto.cnpj = estoque.getFornecedor().getCnpj();
        dto.email  = estoque.getFornecedor().getEmail();
        dto.telefone = estoque.getFornecedor().getTelefone();

        return dto;
    }
}
