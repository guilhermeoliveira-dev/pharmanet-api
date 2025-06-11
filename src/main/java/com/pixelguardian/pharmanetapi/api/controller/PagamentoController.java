package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.PagamentoDTO;
import com.pixelguardian.pharmanetapi.model.entity.Pagamento;
import com.pixelguardian.pharmanetapi.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pagamentos")
@RequiredArgsConstructor
@CrossOrigin
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Pagamento> pagamentos = pagamentoService.getPagamentos();
        return ResponseEntity.ok(pagamentos.stream().map(PagamentoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Pagamento> pagamento = pagamentoService.getPagamentoById(id);
        if (!pagamento.isPresent()) {
            return new ResponseEntity("Pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pagamento.map(PagamentoDTO::create));
    }

    public Pagamento converter(PagamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Pagamento.class);
    }
}
