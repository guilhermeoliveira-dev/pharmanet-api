package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Feedback;
import com.pixelguardian.pharmanetapi.model.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FeedbackService {

    private FeedbackRepository repository;

    public FeedbackService(FeedbackRepository repository) {
        this.repository = repository;
    }

    public List<Feedback> getFeedbacks() {
        return repository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Feedback salvar(Feedback feedback) {
        validar(feedback);
        return repository.save(feedback);
    }

    @Transactional
    public void excluir(Feedback feedback) {
        Objects.requireNonNull(feedback.getId());
        repository.delete(feedback);
    }

    public void validar(Feedback feedback) {
        if (feedback.getComentario() == null || feedback.getComentario().trim().equals("")) {
            throw new RegraNegocioException("Comentário inválido");
        }
        if (feedback.getNota() < 1 || feedback.getNota() > 5) {
            throw new RegraNegocioException("Nota inválida");
        }
        if (feedback.getProduto() == null || feedback.getProduto().getId() == null) {
            throw new RegraNegocioException("Produto inválido");
        }
        if (feedback.getCliente() == null || feedback.getCliente().getId() == null) {
            throw new RegraNegocioException("Cliente inválido");
        }
    }
}
