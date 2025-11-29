package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Funcionario extends Usuario{

//    @Id
//    @GeneratedValue(strategy = )

    private Float salario;
    private String expediente;

    @ManyToOne
    private Cargo cargo;

    @ManyToOne
    private Farmacia farmacia;

    @Override
    @ElementCollection
    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if(cargo.getNome().equals("Administrador")){
            roles.add("ADMIN");
        }
        return roles;
    }
}
