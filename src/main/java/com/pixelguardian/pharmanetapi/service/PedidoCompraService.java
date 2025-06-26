package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.model.repository.PedidoCompraRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PedidoCompraService {

    private PedidoCompraRepository repository;

    public PedidoCompraService(PedidoCompraRepository repository) {
        this.repository = repository;
    }

    public List<PedidoCompra> getPedidoCompras() {
        return repository.findAll();
    }



    public Optional<PedidoCompra> getPedidoCompraById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PedidoCompra salvar(PedidoCompra pedidoCompra) {
        validar(pedidoCompra);
        return repository.save(pedidoCompra);
    }

    @Transactional
    public void excluir(PedidoCompra pedidoCompra) {
        Objects.requireNonNull(pedidoCompra.getId());
        repository.delete(pedidoCompra);
    }

    public void validar(PedidoCompra pedidoCompra) {
        if (pedidoCompra.getValorTotal() == null) {
            throw new RegraNegocioException("Valor Total inválido");
        }
        if (pedidoCompra.getEndereco() == null || pedidoCompra.getEndereco().getId() == null || pedidoCompra.getEndereco().getId() == 0) {
            throw new RegraNegocioException("Endereço inválido");
        }
        if (pedidoCompra.getCodigo() == null || pedidoCompra.getCodigo().trim().equals("")) {
            throw new RegraNegocioException("Código inválido");
        }
        if (pedidoCompra.getDataCriacao() == null || pedidoCompra.getDataCriacao().trim().equals("")) {
            //TODO: data de criação pode ter uma checagem de formato DD/MM/YYYY
            throw new RegraNegocioException("Data de Criação inválida");
        }
        if (pedidoCompra.getStatus() == null || pedidoCompra.getStatus().trim().equals("")) {
            throw new RegraNegocioException("Status do Pedido inválido");
        }
        if (pedidoCompra.getTipoEntrega() == null || pedidoCompra.getTipoEntrega().trim().equals("")) {
            throw new RegraNegocioException("Tipo de Entrega inválido");
        }
        if (pedidoCompra.getStatusEntrega() == null || pedidoCompra.getStatusEntrega().trim().equals("")) {
            throw new RegraNegocioException("Status de Entrega inválido");
        }
    }
}
