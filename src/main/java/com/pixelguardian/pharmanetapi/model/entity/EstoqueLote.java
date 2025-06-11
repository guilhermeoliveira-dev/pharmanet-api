package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("estoqueLote")
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueLote extends Estoque {

    private String lote;
    private String dataFabricacao;
    private String dataValidade;
}
