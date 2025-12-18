package com.pixelguardian.pharmanetapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Cliente extends Usuario {

    private Float fidelidadePontos;

    @Override
    @ElementCollection
    public List<String> getRoles() {
        return new ArrayList<>(Collections.singleton("USER"));
    }

    public Cliente(Long id, String nome, String email, String senha, String cpf, String telefone, String dataAdmissao, Endereco endereco, Float fidelidadePontos) {
        super(id, nome, email, senha, cpf, telefone, dataAdmissao, endereco);
        this.fidelidadePontos = fidelidadePontos;
    }
}
