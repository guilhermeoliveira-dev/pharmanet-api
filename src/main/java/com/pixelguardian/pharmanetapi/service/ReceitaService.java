package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Receita;
import com.pixelguardian.pharmanetapi.model.repository.ReceitaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReceitaService {

    private ReceitaRepository repository;

    public ReceitaService(ReceitaRepository repository) {
        this.repository = repository;
    }

    public List<Receita> getReceitas() {
        return repository.findAll();
    }

    public Optional<Receita> getReceitaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Receita salvar(Receita receita) {
        validar(receita);
        return repository.save(receita);
    }

    @Transactional
    public void excluir(Receita receita) {
        Objects.requireNonNull(receita.getId());
        repository.delete(receita);
    }

    public void validar(Receita receita) {
        if (receita.getMedico() == null || receita.getMedico().trim().equals("")) {
            throw new RegraNegocioException("Médico inválido");
        }
        if (receita.getDataEmissao() == null || receita.getDataEmissao().trim().equals("")) {
            throw new RegraNegocioException("Data de Emissão inválida");
        }
        if (receita.getValidade() == null || receita.getValidade().trim().equals("")) {
            throw new RegraNegocioException("Validade inválida");
        }
        if (receita.getItemPrescrito() == null || receita.getItemPrescrito().trim().equals("")) {
            throw new RegraNegocioException("Item Prescrito inválida");
        }
        if (receita.getAprovado() == null) {
            throw new RegraNegocioException("Aprovado inválido");
        }
        if (receita.getFuncionario() == null || receita.getFuncionario().getId() == null || receita.getFuncionario().getId() == 0) {
            throw new RegraNegocioException("Funcionário inválido");
        }
    }
}
