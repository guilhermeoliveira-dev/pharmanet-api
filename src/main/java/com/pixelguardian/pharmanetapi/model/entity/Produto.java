package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String descricao;
    private Float preco;
    //private NaoSei imagem;
    private Boolean requerLote;
    private Float peso;
    private Boolean generico;

    @ManyToOne
    private Tarja tarja;

    @ManyToOne
    private Categoria categoria;
}
