package com.pixelguardian.pharmanetapi.model.repository;

import com.pixelguardian.pharmanetapi.model.entity.Estoque;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    @Query("select e from Estoque e where e.produto = ?1")
    List<Estoque> findEstoqueByProduto(Produto produto);



}