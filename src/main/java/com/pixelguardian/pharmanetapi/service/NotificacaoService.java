package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Notificacao;
import com.pixelguardian.pharmanetapi.model.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NotificacaoService {

    private NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    public List<Notificacao> getNotificacaos() {
        return repository.findAll();
    }

    public Optional<Notificacao> getNotificacaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Notificacao salvar(Notificacao notificacao) {
        validar(notificacao);
        return repository.save(notificacao);
    }

    @Transactional
    public void excluir(Notificacao notificacao) {
        Objects.requireNonNull(notificacao.getId());
        repository.delete(notificacao);
    }

    public void validar(Notificacao notificacao) {
        if (notificacao.getUsuario() == null || notificacao.getUsuario().getId() == null || notificacao.getUsuario().getId() == 0) {
            throw new RegraNegocioException("Usuário inválido");
        }
        if (notificacao.getMensagem() == null || notificacao.getMensagem().trim().equals("")) {
            throw new RegraNegocioException("Mensagem inválida");
        }
        if (notificacao.getTipoNotificacao() == null || notificacao.getTipoNotificacao().trim().equals("")) {
            throw new RegraNegocioException("Tipo de Notificação inválido");
        }
    }
}