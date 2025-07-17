package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.entity.PermissaoIndividual;
import com.pixelguardian.pharmanetapi.model.repository.PermissaoIndividualRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PermissaoIndividualService {

    private PermissaoIndividualRepository repository;

    public PermissaoIndividualService(PermissaoIndividualRepository repository) {
        this.repository = repository;
    }

    public List<PermissaoIndividual> findByCargo(Cargo cargo){
        return repository.findByCargo(cargo);
    }

    public List<PermissaoIndividual> getPermissaoIndividuals() {
        return repository.findAll();
    }

    public Optional<PermissaoIndividual> getPermissaoIndividualById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PermissaoIndividual salvar(PermissaoIndividual permissao) {
        validar(permissao);
        return repository.save(permissao);
    }

    @Transactional
    public int deleteByCargo(Cargo cargo){
        return repository.deleteByCargo(cargo);
    }

    @Transactional
    public void excluir(PermissaoIndividual permissao) {
        Objects.requireNonNull(permissao.getId());
        repository.delete(permissao);
    }

    public void validar(PermissaoIndividual permissao) {
        if (permissao.getCargo() == null || permissao.getCargo().getId() == null || permissao.getCargo().getId() == 0) {
            throw new RegraNegocioException("Cargo inválido");
        }
        if (permissao.getPermissao() == null || permissao.getPermissao().getId() == null || permissao.getPermissao().getId() == 0) {
            throw new RegraNegocioException("Permissão inválida");
        }
    }
}
