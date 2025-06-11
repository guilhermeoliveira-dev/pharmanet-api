package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Tarja;
import com.pixelguardian.pharmanetapi.model.repository.TarjaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TarjaService {

    private TarjaRepository repository;

    public TarjaService(TarjaRepository repository) {
        this.repository = repository;
    }

    public List<Tarja> getTarjas() {
        return repository.findAll();
    }

    public Optional<Tarja> getTarjaById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Tarja salvar(Tarja tarja) {
        validar(tarja);
        return repository.save(tarja);
    }

    @Transactional
    public void excluir(Tarja tarja) {
        Objects.requireNonNull(tarja.getId());
        repository.delete(tarja);
    }

    public void validar(Tarja tarja) {
        if (tarja == null || tarja.getId() == 0) {
            throw new RegraNegocioException("Tarja Inválida");
        }
    }
}
