package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.EstoqueDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.EstoqueService;
import com.pixelguardian.pharmanetapi.service.FarmaciaService;
import com.pixelguardian.pharmanetapi.service.FornecedorService;
import com.pixelguardian.pharmanetapi.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/estoques")
@RequiredArgsConstructor
@CrossOrigin
public class EstoqueController {

    private final EstoqueService estoqueService;
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;
    private final FarmaciaService farmaciaService;

    @GetMapping("")
    public ResponseEntity get(){
        List<Estoque> estoques = estoqueService.getEstoques();
        return ResponseEntity.ok(estoques.stream().map(EstoqueDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Estoque> estoque = estoqueService.getEstoqueById(id);
        if (!estoque.isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(estoque.map(EstoqueDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(EstoqueDTO dto) {
        try {
            Estoque estoque = converter(dto);
            estoque = estoqueService.salvar(estoque);
            return new ResponseEntity(estoque, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, EstoqueDTO dto) {
        if (!estoqueService.getEstoqueById(id).isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Estoque estoque = converter(dto);
            estoque.setId(id);
            estoqueService.salvar(estoque);
            return ResponseEntity.ok(estoque);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Estoque> estoque = estoqueService.getEstoqueById(id);
        if (!estoque.isPresent()) {
            return new ResponseEntity("Estoque não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            estoqueService.excluir(estoque.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Estoque converter(EstoqueDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Estoque estoque;
        if ("estoqueLote".equalsIgnoreCase(dto.getType())) {
            estoque = modelMapper.map(dto, EstoqueLote.class);
        } else {
            estoque = modelMapper.map(dto, Estoque.class);
        }
        if (dto.getIdProduto() != null) {
            Optional<Produto> produto = produtoService.getProdutoById((dto.getIdProduto()));
            if (produto.isPresent()) {
                estoque.setProduto(produto.get());
            } else {
                estoque.setProduto(null);
            }
        }
        if (dto.getIdFornecedor() != null) {
            Optional<Fornecedor> fornecedor = fornecedorService.getFornecedorById((dto.getIdFornecedor()));
            if (fornecedor.isPresent()) {
                estoque.setFornecedor(fornecedor.get());
            } else {
                estoque.setFornecedor(null);
            }
        }
        if (dto.getIdFarmacia() != null) {
            Optional<Farmacia> farmacia = farmaciaService.getFarmaciaById((dto.getIdFarmacia()));
            if (farmacia.isPresent()) {
                estoque.setFarmacia(farmacia.get());
            } else {
                estoque.setFarmacia(null);
            }
        }
        return estoque;
    }
}
