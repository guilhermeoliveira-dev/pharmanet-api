package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.EstoqueLote;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import com.pixelguardian.pharmanetapi.model.repository.EstoqueLoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EstoqueLoteService {

    private EstoqueLoteRepository repository;

    public EstoqueLoteService(EstoqueLoteRepository repository) {
        this.repository = repository;
    }

    public List<EstoqueLote> getEstoqueLotes() {
        return repository.findAll();
    }

    public Optional<EstoqueLote> getEstoqueLoteById(Long id) {
        return repository.findById(id);
    }

    public Optional<EstoqueLote> acharEstoquePorProduto(Produto produto){
        Optional<EstoqueLote> estoque = Optional.empty();
        for (EstoqueLote estoqueObj : repository.findByProdutoAndQuantidadeNot(produto, 0)){
            if(estoque.isPresent()){
                if (estoque.get().getDataValidade().isBefore(estoqueObj.getDataValidade())){
                    estoque = Optional.of(estoqueObj);
                }
            }else{
                estoque = Optional.of(estoqueObj);
            }
        }
        return estoque;
    }

    @Transactional
    public EstoqueLote salvar(EstoqueLote estoqueLote) {
        validar(estoqueLote);
        return repository.save(estoqueLote);
    }

    @Transactional
    public void excluir(EstoqueLote estoqueLote) {
        Objects.requireNonNull(estoqueLote.getId());
        repository.delete(estoqueLote);
    }

    public void validar(EstoqueLote estoqueLote) {
        if (estoqueLote.getLote() == null || estoqueLote.getLote().trim().equals("")) {
            throw new RegraNegocioException("Lote inválido");
        }
        if (estoqueLote.getDataFabricacao() == null || estoqueLote.getDataFabricacao().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Data de fabricação inválida");
        }
        if (estoqueLote.getDataValidade() == null || estoqueLote.getDataValidade().isAfter(LocalDate.now())) {
            throw new RegraNegocioException("Data de validade inválida");
        }
    }
}
