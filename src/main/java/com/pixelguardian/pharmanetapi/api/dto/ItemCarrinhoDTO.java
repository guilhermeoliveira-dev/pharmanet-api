package com.pixelguardian.pharmanetapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoDTO {
    private Long idProduto;
    private Long idReceita;
    private Integer quantidade;
    private Float precoUnitario; // Necessário para evitar inconsistência (Issue 6)
}