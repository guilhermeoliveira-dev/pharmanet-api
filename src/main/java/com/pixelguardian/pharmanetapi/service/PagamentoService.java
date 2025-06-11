package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Pagamento;
import com.pixelguardian.pharmanetapi.model.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PagamentoService {

    private PagamentoRepository repository;

    public PagamentoService(PagamentoRepository repository) {
        this.repository = repository;
    }

    public List<Pagamento> getPagamentos() {
        return repository.findAll();
    }

    public Optional<Pagamento> getPagamentoById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Pagamento salvar(Pagamento pagamento) {
        validar(pagamento);
        return repository.save(pagamento);
    }

    @Transactional
    public void excluir(Pagamento pagamento) {
        Objects.requireNonNull(pagamento.getId());
        repository.delete(pagamento);
    }

    public void validar(Pagamento pagamento) {
        if (pagamento.getValor() == null) {
            throw new RegraNegocioException("Valor inválido");
        }
        if (pagamento.getDataPagamento() == null || pagamento.getDataPagamento().trim().equals("")) {
            //TODO: talvez possamos fazer uma checagem mais complexa verificando se a data está no formato DD/MM/YYYY
            throw new RegraNegocioException("Data de Pagamento inválida");
        }
        if (pagamento.getFormaPagamento() == null || pagamento.getFormaPagamento().trim().equals("")) {
            //TODO: podemos definir melhor as formas de pagamento, talvez um enum seria o ideal.
            throw new RegraNegocioException("Forma de Pagamento inválida");
        }
    }
}
