package com.pixelguardian.pharmanetapi.model.repository;

import com.pixelguardian.pharmanetapi.model.entity.ItemPedido;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    List<ItemPedido> findByPedidoCompra(PedidoCompra pedidoCompra);

}
