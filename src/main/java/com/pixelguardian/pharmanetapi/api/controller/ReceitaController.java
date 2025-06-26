package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.ReceitaDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.FuncionarioService;
import com.pixelguardian.pharmanetapi.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
@CrossOrigin
public class ReceitaController {

    private final ReceitaService receitaService;
    private final FuncionarioService funcionarioService;

    @GetMapping()
    public ResponseEntity get() {
        List<Receita> receitas = receitaService.getReceitas();
        return ResponseEntity.ok(receitas.stream().map(ReceitaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Receita> receita = receitaService.getReceitaById(id);
        if (!receita.isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(receita.map(ReceitaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(ReceitaDTO dto) {
        try {
            Receita receita = converter(dto);
            receita = receitaService.salvar(receita);
            return new ResponseEntity(receita, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, ReceitaDTO dto) {
        if (!receitaService.getReceitaById(id).isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Receita receita = converter(dto);
            receita.setId(id);
            receitaService.salvar(receita);
            return ResponseEntity.ok(receita);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Receita> receita = receitaService.getReceitaById(id);
        if (!receita.isPresent()) {
            return new ResponseEntity("Receita não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            receitaService.excluir(receita.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Receita converter(ReceitaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Receita receita = modelMapper.map(dto, Receita.class);
        if (dto.getIdFuncionario() != null) {
            Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdFuncionario());
            if (funcionario.isPresent()) {
                receita.setFuncionario(funcionario.get());
            } else {
                receita.setFuncionario(null);
            }
        }
        return receita;
    }
}
