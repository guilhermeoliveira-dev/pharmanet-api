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

    private static List<String> roles = new ArrayList<>(Collections.singleton("USER"));

    @Override
    @ElementCollection
    public List<String> getRoles() {
        return roles;
    }
}
