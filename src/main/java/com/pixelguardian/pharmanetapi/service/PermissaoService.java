package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.repository.PermissaoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PermissaoService {

    private PermissaoRepository repository;

    public PermissaoService(PermissaoRepository repository) {
        this.repository = repository;
    }

    public List<Permissao> getPermissaos() {
        return repository.findAll();
    }

    public Optional<Permissao> getPermissaoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Permissao salvar(Permissao permissao) {
        validar(permissao);
        return repository.save(permissao);
    }

    @Transactional
    public void excluir(Permissao permissao) {
        Objects.requireNonNull(permissao.getId());
        repository.delete(permissao);
    }

    public void validar(Permissao permissao) {
        if (permissao.getNome() == null || permissao.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (permissao.getCargo() == null || permissao.getCargo().getId() == null || permissao.getCargo().getId() == 0) {
            throw new RegraNegocioException("Produto inválido");
        }
    }
}
