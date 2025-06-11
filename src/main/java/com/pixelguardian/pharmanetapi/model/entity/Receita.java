package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Receita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medico;
    private String dataEmissao;
    private String validade;
    private String itemPrescrito;
    private Boolean aprovado;

    @ManyToOne
    private Funcionario funcionario;
}
