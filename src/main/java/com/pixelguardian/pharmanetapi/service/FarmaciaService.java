package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Farmacia;
import com.pixelguardian.pharmanetapi.model.repository.FarmaciaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FarmaciaService {

    private FarmaciaRepository repository;

    public FarmaciaService(FarmaciaRepository repository) {
        this.repository = repository;
    }

    public List<Farmacia> getFarmacias() {
        return repository.findAll();
    }

    public Optional<Farmacia> getFarmaciaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Farmacia salvar(Farmacia farmacia) {
        validar(farmacia);
        return repository.save(farmacia);
    }

    @Transactional
    public void excluir(Farmacia farmacia) {
        Objects.requireNonNull(farmacia.getId());
        repository.delete(farmacia);
    }

    public void validar(Farmacia farmacia) {
        if (farmacia.getCnpj() == null || farmacia.getCnpj().trim().equals("")) {
            throw new RegraNegocioException("CNPJ inválido");
        }
        if (farmacia.getNome() == null || farmacia.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (farmacia.getEmail() == null || farmacia.getEmail().trim().equals("")) {
            throw new RegraNegocioException("E-mail inválido");
        }
        if (farmacia.getTelefone() == null || farmacia.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if (farmacia.getEndereco() == null || farmacia.getEndereco().getId() == 0) {
            throw new RegraNegocioException("Endereço Inválido");
        }
    }
}
