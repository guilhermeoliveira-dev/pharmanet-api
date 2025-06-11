package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Funcionario;
import com.pixelguardian.pharmanetapi.model.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FuncionarioService {

    private FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> getFuncionarios() {
        return repository.findAll();
    }

    public Optional<Funcionario> getFuncionarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        validar(funcionario);
        return repository.save(funcionario);
    }

    @Transactional
    public void excluir(Funcionario funcionario) {
        Objects.requireNonNull(funcionario.getId());
        repository.delete(funcionario);
    }

    public void validar(Funcionario funcionario) {
        if (funcionario.getNome() == null || funcionario.getNome().trim().equals("")) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (funcionario.getSenha() == null || funcionario.getSenha().trim().equals("")) {
            throw new RegraNegocioException("Senha inválida");
        }
        if (funcionario.getCpf() == null || funcionario.getCpf().trim().equals("")) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (funcionario.getTelefone() == null || funcionario.getTelefone().trim().equals("")) {
            throw new RegraNegocioException("Telefone inválido");
        }
        if (funcionario.getDataAdmissao() == null || funcionario.getDataAdmissao().trim().equals("")) {
            throw new RegraNegocioException("Data de Admissão inválida");
        }
        if (funcionario.getEndereco() == null || funcionario.getEndereco().getId() == null || funcionario.getEndereco().getId() == 0) {
            throw new RegraNegocioException("Endereço inválido");
        }
        if (funcionario.getSalario() == null) {
            //TODO: salário == 0 é exception? fiquei na duvida se isso faz sentido
            //  || funcionario.getSalario() == 0
            throw new RegraNegocioException("Salário inválido");
        }
        if (funcionario.getExpediente() == null || funcionario.getExpediente().trim().equals("")) {
            //TODO: expediente podia ser um enum
            throw new RegraNegocioException("Expediente inválido");
        }
        if (funcionario.getCargo() == null || funcionario.getCargo().getId() == null || funcionario.getCargo().getId() == 0) {
            throw new RegraNegocioException("Cargo inválido");
        }
        if (funcionario.getFarmacia() == null || funcionario.getFarmacia().getId() == null || funcionario.getFarmacia().getId() == 0) {
            throw new RegraNegocioException("Farmácia inválida");
        }
    }
}
