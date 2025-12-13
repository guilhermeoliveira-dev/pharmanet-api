package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String dataCriacao;
    private String dataEntrega;
    private String status;
    private String tipoEntrega;
    private String statusEntrega;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Endereco endereco;

    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ItemPedido> itensPedido = new ArrayList<>();

    @OneToOne(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private Venda venda;
}