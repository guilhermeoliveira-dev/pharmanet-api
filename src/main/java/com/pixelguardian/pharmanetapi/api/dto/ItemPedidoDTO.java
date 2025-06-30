package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.ItemPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {

    private Long id;
    private Integer quantidade;
    private Float precoUnitario;

    private Long idEstoque;
    private Integer quantidadeEstoque;
    private Long idProduto;
    private String nomeProduto;
    private Long idFornecedor;
    private Long idFarmacia;

    private Long idPedidoCompra;
    private String codigo;
    private String dataCriacao;
    private String status;
    private Float valorTotal;
    private String tipoEntrega;
    private String statusEntrega;
    private String dataEntrega;
    private Long idEndereco;

    private Long idReceita;
    private String medico;
    private String dataEmissao;
    private String validade;
    private String itemPrescrito;
    private Boolean aprovado;
    private Long idfuncionario;

    public static ItemPedidoDTO create(ItemPedido itemPedido) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedidoDTO dto = modelMapper.map(itemPedido, ItemPedidoDTO.class);

        dto.idEstoque = itemPedido.getEstoque().getId();
        dto.quantidadeEstoque = itemPedido.getEstoque().getQuantidade();
        dto.idProduto = itemPedido.getEstoque().getProduto().getId();
        dto.nomeProduto = itemPedido.getEstoque().getProduto().getNome();
        dto.idFornecedor = itemPedido.getEstoque().getFornecedor().getId();
        dto.idFarmacia = itemPedido.getEstoque().getFarmacia().getId();

        dto.idPedidoCompra = itemPedido.getPedidoCompra().getId();
        dto.codigo = itemPedido.getPedidoCompra().getCodigo();
        dto.dataCriacao = itemPedido.getPedidoCompra().getDataCriacao();
        dto.status = itemPedido.getPedidoCompra().getStatus();
        dto.valorTotal = itemPedido.getPedidoCompra().getValorTotal();
        dto.tipoEntrega = itemPedido.getPedidoCompra().getTipoEntrega();
        dto.statusEntrega = itemPedido.getPedidoCompra().getStatusEntrega();
        dto.dataEntrega = itemPedido.getPedidoCompra().getDataEntrega();
        if (itemPedido.getPedidoCompra().getEndereco() != null){
            dto.idEndereco = itemPedido.getPedidoCompra().getEndereco().getId();
        }

//        Receitas podem ser nulas, é um campo opcional. Se não verificarmos dá nullPointerException
        if (itemPedido.getReceita() != null){
            dto.idReceita = itemPedido.getReceita().getId();
            dto.medico = itemPedido.getReceita().getMedico();
            dto.dataEmissao = itemPedido.getReceita().getDataEmissao();
            dto.validade = itemPedido.getReceita().getValidade();
            dto.itemPrescrito = itemPedido.getReceita().getItemPrescrito();
            dto.aprovado = itemPedido.getReceita().getAprovado();
            dto.idfuncionario = itemPedido.getReceita().getFuncionario().getId();
        }

        return dto;
    }
}
