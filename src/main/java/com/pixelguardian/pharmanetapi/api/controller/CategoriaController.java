package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.CategoriaDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Categoria;
import com.pixelguardian.pharmanetapi.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
@CrossOrigin
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Categoria> categorias = categoriaService.getCategorias();
        return ResponseEntity.ok(categorias.stream().map(CategoriaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Categoria> categoria = categoriaService.getCategoriaById(id);
        if (!categoria.isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(categoria.map(CategoriaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(CategoriaDTO dto) {
        try {
            Categoria categoria = converter(dto);
            categoria = categoriaService.salvar(categoria);
            return new ResponseEntity(categoria, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, CategoriaDTO dto) {
        if (!categoriaService.getCategoriaById(id).isPresent()) {
            return new ResponseEntity("Categoria não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Categoria categoria = converter(dto);
            categoria.setId(id);
            categoriaService.salvar(categoria);
            return ResponseEntity.ok(categoria);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Categoria converter(CategoriaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        //modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        Categoria categoria = modelMapper.map(dto, Categoria.class);
        if (dto.getIdCategoriaPai() != null) {
            Optional<Categoria> categoriaPai = categoriaService.getCategoriaById(dto.getIdCategoriaPai());
            if (categoriaPai.isPresent()) {
                categoria.setCategoriaPai(categoriaPai.get());
            } else {
                categoria.setCategoriaPai(null);
            }
        }
        return categoria;
    }
}
