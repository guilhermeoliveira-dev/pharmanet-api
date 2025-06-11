package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidade;
    private Float precoUnitario;

    @ManyToOne
    private Estoque estoque;

    @ManyToOne
    private PedidoCompra pedidoCompra;

    @OneToOne
    private Receita receita;
}
