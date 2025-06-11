package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Float preco;
    //private NaoSei imagem;
    private Boolean requerLote;
    private Float peso;
    private Boolean generico;

    private Long idTarja;
    private String nomeTarja;
    private String cor;
    private Boolean requerReceita;
    private Boolean retemReceita;

    private Long idCategoria;
    private String nomeCategoria;
    private String descricaoCategoria;
    //Descenecessário pro produto, e causa problemas
//    private Long idCategoriaPai;

    public static ProdutoDTO create(Produto produto) {
        ModelMapper modelMapper = new ModelMapper();
        ProdutoDTO dto = modelMapper.map(produto, ProdutoDTO.class);
        dto.idTarja = produto.getTarja().getId();
        dto.nomeTarja = produto.getTarja().getNome();
        dto.cor = produto.getTarja().getCor();
        dto.requerReceita = produto.getTarja().getRequerReceita();
        dto.retemReceita = produto.getTarja().getRetemReceita();

        dto.idCategoria = produto.getCategoria().getId();
        dto.nomeCategoria = produto.getCategoria().getNome();
        dto.descricaoCategoria = produto.getCategoria().getDescricao();
//        dto.idCategoriaPai = produto.getCategoria().getCategoriaPai().getId();
        return dto;
    }
}