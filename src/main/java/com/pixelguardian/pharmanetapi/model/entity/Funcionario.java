package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "usuario_id") // Define o nome da coluna de join
public class Funcionario extends Usuario{

//    @Id
//    @GeneratedValue(strategy = )

    private Float salario;
    private String expediente;

    @ManyToOne
    private Cargo cargo;

    @ManyToOne
    private Farmacia farmacia;
}
