package com.pixelguardian.pharmanetapi.model.repository;

import com.pixelguardian.pharmanetapi.model.entity.ItemPedido;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoCompraRepository extends JpaRepository<PedidoCompra, Long> {
    @Query("select (count(p) > 0) from PedidoCompra p where p.codigo = ?1")
    boolean existsByCodigo(String codigo);




}
