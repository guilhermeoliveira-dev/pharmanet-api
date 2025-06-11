package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Estoque;
import com.pixelguardian.pharmanetapi.model.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EstoqueService {

    private EstoqueRepository repository;

    public EstoqueService(EstoqueRepository repository) {
        this.repository = repository;
    }

    public List<Estoque> getEstoques() {
        return repository.findAll();
    }

    public Optional<Estoque> getEstoqueById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Estoque salvar(Estoque estoque) {
        validar(estoque);
        return repository.save(estoque);
    }

    @Transactional
    public void excluir(Estoque estoque) {
        Objects.requireNonNull(estoque.getId());
        repository.delete(estoque);
    }

    public void validar(Estoque estoque) {
        if (estoque.getQuantidade() == null) { // Removi o quantidade == 0, pq n faz sentido, pode ser que o estoque esteja vazio
            throw new RegraNegocioException("Quantidade inválida");
        }
        if (estoque.getProduto() == null || estoque.getProduto().getId() == null || estoque.getProduto().getId() == 0) {
            throw new RegraNegocioException("Produto inválido");
        }
        if (estoque.getFornecedor() == null || estoque.getFornecedor().getId() == null || estoque.getFornecedor().getId() == 0) {
            throw new RegraNegocioException("Fornecedor inválido");
        }
        if (estoque.getFarmacia() == null || estoque.getFarmacia().getId() == null || estoque.getFarmacia().getId() == 0) {
            throw new RegraNegocioException("Farmácia inválida");
        }
    }
}
