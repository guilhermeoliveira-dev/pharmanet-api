package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.FornecedorDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.model.entity.Fornecedor;
import com.pixelguardian.pharmanetapi.service.FornecedorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/fornecedores")
@RequiredArgsConstructor
@CrossOrigin
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping("")
    public ResponseEntity get(){
        List<Fornecedor> fornecedors = fornecedorService.getFornecedores();
        return ResponseEntity.ok(fornecedors.stream().map(FornecedorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = fornecedorService.getFornecedorById(id);
        if (!fornecedor.isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(fornecedor.map(FornecedorDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(FornecedorDTO dto) {
        try {
            Fornecedor fornecedor = converter(dto);
            fornecedor = fornecedorService.salvar(fornecedor);
            return new ResponseEntity(fornecedor, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, FornecedorDTO dto) {
        if (!fornecedorService.getFornecedorById(id).isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Fornecedor fornecedor = converter(dto);
            fornecedor.setId(id);
            fornecedorService.salvar(fornecedor);
            return ResponseEntity.ok(fornecedor);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Fornecedor> fornecedor = fornecedorService.getFornecedorById(id);
        if (!fornecedor.isPresent()) {
            return new ResponseEntity("Fornecedor não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            fornecedorService.excluir(fornecedor.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Fornecedor converter(FornecedorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Fornecedor fornecedor = modelMapper.map(dto, Fornecedor.class);
        Endereco endereco = modelMapper.map(dto, Endereco.class);
        fornecedor.setEndereco(endereco);
        return fornecedor;
    }
}
