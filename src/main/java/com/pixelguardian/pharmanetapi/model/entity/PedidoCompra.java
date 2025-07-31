package com.pixelguardian.pharmanetapi.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String dataCriacao;
    private String status;
    private String tipoEntrega;
    private String statusEntrega;
    private String dataEntrega;

    @ManyToOne
    private Endereco endereco;

    @ManyToOne
    private Usuario usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "pedidoCompra", fetch = FetchType.LAZY)
    private List<ItemPedido> itensPedido;
}