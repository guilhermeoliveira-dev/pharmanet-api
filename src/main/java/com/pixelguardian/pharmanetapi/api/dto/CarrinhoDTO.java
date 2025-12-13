package com.pixelguardian.pharmanetapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoDTO {
    private Long idUsuario;
    private String tipoEntrega;
    private List<ItemCarrinhoDTO> itens;
}