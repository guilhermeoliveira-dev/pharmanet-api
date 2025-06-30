package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.Venda;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaDTO {

    private Long id;
    private String dataVenda;

    private Long idPagamento;
    private String dataPagamento;
    private Float valor;
    private String formaPagamento;

    private Long idPedidoCompra;
    private String codigo;
    private String dataCriacao;
    private String status;
    private Float valorTotal;
    private String tipoEntrega;
    private String statusEntrega;
    private String dataEntrega;

    public static VendaDTO create(Venda venda) {
        ModelMapper modelMapper = new ModelMapper();
        VendaDTO dto = modelMapper.map(venda, VendaDTO.class);

        dto.idPedidoCompra = venda.getPedidoCompra().getId();
        dto.codigo = venda.getPedidoCompra().getCodigo();
        dto.dataCriacao = venda.getPedidoCompra().getDataCriacao();
        dto.status = venda.getPedidoCompra().getStatus();
        dto.valorTotal = venda.getPedidoCompra().getValorTotal();
        dto.tipoEntrega = venda.getPedidoCompra().getTipoEntrega();
        dto.statusEntrega = venda.getPedidoCompra().getStatusEntrega();
        dto.dataEntrega = venda.getPedidoCompra().getDataEntrega();

        return dto;
    }
}