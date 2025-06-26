package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.ItemPedido;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.model.repository.ItemPedidoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemPedidoService {

    private ItemPedidoRepository repository;

    public ItemPedidoService(ItemPedidoRepository repository) {
        this.repository = repository;
    }

    public List<ItemPedido> getItemPedidos() {
        return repository.findAll();
    }

    public List<ItemPedido> getItemPedidosByPedidoCompra(PedidoCompra pedidoCompra) {
        return repository.findByPedidoCompra(pedidoCompra);
    }

    public Optional<ItemPedido> getItemPedidoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public ItemPedido salvar(ItemPedido itemPedido) {
        validar(itemPedido);
        return repository.save(itemPedido);
    }

    @Transactional
    public void excluir(ItemPedido itemPedido) {
        Objects.requireNonNull(itemPedido.getId());
        repository.delete(itemPedido);
    }

    public void validar(ItemPedido itemPedido) {
        if (itemPedido.getQuantidade() == null || itemPedido.getQuantidade() == 0) {
            throw new RegraNegocioException("Quantidade inválida");
        }
        if (itemPedido.getPrecoUnitario() == null || itemPedido.getPrecoUnitario() == 0) {
            throw new RegraNegocioException("Preço unitário inválido");
        }
        if (itemPedido.getEstoque() == null || itemPedido.getEstoque().getId() == null) {
            throw new RegraNegocioException("Estoque inválido");
        }
        if (itemPedido.getPedidoCompra() == null || itemPedido.getPedidoCompra().getId() == null) {
            throw new RegraNegocioException("Pedido de compra inválido");
        }
//        if (itemPedido.getReceita() == null || itemPedido.getReceita().getId() == null) {
//            throw new RegraNegocioException("Receita inválida");
//        }
    }
}
