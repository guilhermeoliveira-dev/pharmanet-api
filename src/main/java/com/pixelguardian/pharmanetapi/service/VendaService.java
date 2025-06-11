package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Venda;
import com.pixelguardian.pharmanetapi.model.repository.VendaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VendaService {

    private VendaRepository repository;

    public VendaService(VendaRepository repository) {
        this.repository = repository;
    }

    public List<Venda> getVendas() {
        return repository.findAll();
    }

    public Optional<Venda> getVendaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Venda salvar(Venda venda) {
        validar(venda);
        return repository.save(venda);
    }

    @Transactional
    public void excluir(Venda venda) {
        Objects.requireNonNull(venda.getId());
        repository.delete(venda);
    }

    public void validar(Venda venda) {
        if (venda.getDataVenda() == null || venda.getDataVenda().trim().equals("")) {
            throw new RegraNegocioException("Data da Venda inválida");
        }
        if (venda.getPagamento() == null || venda.getPagamento().getId() == null || venda.getPagamento().getId() == 0) {
            throw new RegraNegocioException("Pagamento inválido");
        }
        if (venda.getPedidoCompra() == null || venda.getPedidoCompra().getId() == null || venda.getPedidoCompra().getId() == 0) {
            throw new RegraNegocioException("Pedido de Compra inválido");
        }
    }
}
