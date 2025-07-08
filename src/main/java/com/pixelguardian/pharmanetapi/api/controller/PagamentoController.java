package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.CategoriaDTO;
import com.pixelguardian.pharmanetapi.api.dto.PagamentoDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Categoria;
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

    @PostMapping()
    public ResponseEntity post(@RequestBody PagamentoDTO dto) {
        try {
            Pagamento pagamento = converter(dto);
            pagamento = pagamentoService.salvar(pagamento);
            return new ResponseEntity(pagamento, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PagamentoDTO dto) {
        if (!pagamentoService.getPagamentoById(id).isPresent()) {
            return new ResponseEntity("Pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Pagamento pagamento = converter(dto);
            pagamento.setId(id);
            pagamentoService.salvar(pagamento);
            return ResponseEntity.ok(pagamento);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Pagamento> pagamento = pagamentoService.getPagamentoById(id);
        if (!pagamento.isPresent()) {
            return new ResponseEntity("Pagamento não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            pagamentoService.excluir(pagamento.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Pagamento converter(PagamentoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Pagamento.class);
    }
}
