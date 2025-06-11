package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import com.pixelguardian.pharmanetapi.model.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService {

    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public List<Produto> getProdutos() {
        return repository.findAll();
    }

    public Optional<Produto> getProdutoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        validar(produto);
        return repository.save(produto);
    }

    @Transactional
    public void excluir(Produto produto) {
        Objects.requireNonNull(produto.getId());
        repository.delete(produto);
    }

    public void validar(Produto produto) {
        if (produto.getPreco() == null) {
            throw new RegraNegocioException("Preço inválida");
        }
        if (produto.getPeso() == null) {
            throw new RegraNegocioException("Peso inválida");
        }
        if (produto.getRequerLote() == null) {
            throw new RegraNegocioException("Requer Lote Inválido");
        }
        if (produto.getGenerico() == null) {
            throw new RegraNegocioException("Genérico Inválido");
        }
        if (produto.getNome() == null || produto.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (produto.getDescricao() == null) {
            //  || produto.getDescricao().trim().equals("")
            // descrição pode ser vazia? Se não, adicionar a linha acima na condição
            throw new RegraNegocioException("Descrição inválida");
        }
        if (produto.getTarja() == null || produto.getTarja().getId() == null || produto.getTarja().getId() == 0) {
            throw new RegraNegocioException("Tarja Inválida");
        }
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null || produto.getCategoria().getId() == 0) {
            throw new RegraNegocioException("Categoria Inválida");
        }
    }
}
