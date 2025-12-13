package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Estoque;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import com.pixelguardian.pharmanetapi.model.repository.EstoqueRepository;
import lombok.RequiredArgsConstructor; // Adicionado para injeção via construtor (melhor prática)
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository repository;

    public List<Estoque> getEstoques() {
        return repository.findAll();
    }

    public Optional<Estoque> getEstoqueById(Long id) {
        return repository.findById(id);
    }

    public List<Estoque> getEstoqueByProdutoId(Long idProduto) {
        return repository.findByProdutoId(idProduto);
    }

    public List<Estoque> findEstoqueByProduto(Produto produto){
        return repository.findEstoqueByProduto(produto);
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
        if (estoque.getQuantidade() == null) {
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