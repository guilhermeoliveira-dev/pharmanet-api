package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Fornecedor;
import com.pixelguardian.pharmanetapi.model.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FornecedorService {

    private FornecedorRepository repository;

    public FornecedorService(FornecedorRepository repository) {
        this.repository = repository;
    }

    public List<Fornecedor> getFornecedores() {
        return repository.findAll();
    }

    public Optional<Fornecedor> getFornecedorById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Fornecedor salvar(Fornecedor fornecedor) {
        validar(fornecedor);
        return repository.save(fornecedor);
    }

    @Transactional
    public void excluir(Fornecedor fornecedor) {
        Objects.requireNonNull(fornecedor.getId());
        repository.delete(fornecedor);
    }

    public void validar(Fornecedor fornecedor) {
        if (fornecedor.getNome() == null || fornecedor.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().trim().equals("")) {
            throw new RegraNegocioException("CNPJ inválido");
        }
        if (fornecedor.getEmail() == null || fornecedor.getEmail().trim().equals("")) {
            throw new RegraNegocioException("E-mail inválido");
        }
        if (fornecedor.getTelefone() == null || fornecedor.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if (fornecedor.getEndereco() == null || fornecedor.getEndereco().getId() == null || fornecedor.getEndereco().getId() == 0) {
            throw new RegraNegocioException("Fonecedor inválido");
        }
    }
}
