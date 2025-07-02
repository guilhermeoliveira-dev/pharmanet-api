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
    private String nome;
//    private String descricao;
//    private Float preco;
//    private NaoSei imagem;
//    private Boolean requerLote;
//    private Float peso;
//    private Boolean generico;

    private Long idFornecedor;
    private String nomeFornecedor;
    private String cnpjFornecedor;
    private String emailFornecedor;
    private String telefoneFornecedor;

    private Long idFarmacia;
    private String cnpjFarmacia;
    private String nomeFarmacia;
    private String emailFarmacia;
    private String telefoneFarmacia;
    //private NaoSei imagemLogo;

    public static EstoqueDTO create(Estoque estoque) {
        ModelMapper modelMapper = new ModelMapper();
        EstoqueDTO dto = modelMapper.map(estoque, EstoqueDTO.class);

        dto.idProduto = estoque.getProduto().getId();
        dto.nome = estoque.getProduto().getNome();

        dto.idFornecedor = estoque.getFornecedor().getId();
        dto.nomeFornecedor = estoque.getFornecedor().getNome();
        dto.cnpjFornecedor = estoque.getFornecedor().getCnpj();
        dto.emailFornecedor = estoque.getFornecedor().getEmail();
        dto.telefoneFornecedor = estoque.getFornecedor().getTelefone();

        dto.idFarmacia = estoque.getFarmacia().getId();
        dto.nomeFarmacia = estoque.getFarmacia().getNome();
        dto.cnpjFarmacia = estoque.getFarmacia().getCnpj();
        dto.emailFarmacia = estoque.getFarmacia().getEmail();
        dto.telefoneFarmacia = estoque.getFarmacia().getTelefone();

        return dto;
    }
}
