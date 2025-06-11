package com.pixelguardian.pharmanetapi.api.dto;

import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCompraDTO {

    private String codigo;
    private String dataCriacao;
    private String status;
    private Float valorTotal;
    private String tipoEntrega;
    private String statusEntrega;
    private String dataEntrega;

    private Long idEndereco;
    private String uf;
    private String cidade;
    private String cep;
    private String bairro;
    private String logradouro;
    private String numero;
    private String complemento;

    public static PedidoCompraDTO create(PedidoCompra pedidoCompra) {
        ModelMapper modelMapper = new ModelMapper();
        PedidoCompraDTO dto = modelMapper.map(pedidoCompra, PedidoCompraDTO.class);
        if (pedidoCompra.getEndereco() != null){
            dto.logradouro = pedidoCompra.getEndereco().getLogradouro();
            dto.numero = pedidoCompra.getEndereco().getNumero();
            dto.complemento = pedidoCompra.getEndereco().getComplemento();
            dto.bairro = pedidoCompra.getEndereco().getBairro();
            dto.cidade = pedidoCompra.getEndereco().getCidade();
            dto.uf = pedidoCompra.getEndereco().getUf();
            dto.cep = pedidoCompra.getEndereco().getCep();
        }
        return dto;
    }
}