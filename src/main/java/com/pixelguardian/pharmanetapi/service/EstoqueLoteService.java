package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.EstoqueLote;
import com.pixelguardian.pharmanetapi.model.repository.EstoqueLoteRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        if (estoqueLote.getDataFabricacao() == null || estoqueLote.getDataFabricacao().trim().equals("")) {
            throw new RegraNegocioException("Data de fabricação inválida");
        }
        if (estoqueLote.getDataValidade() == null || estoqueLote.getDataValidade().trim().equals("")) {
            throw new RegraNegocioException("Data de validade inválida");
        }
    }
}
