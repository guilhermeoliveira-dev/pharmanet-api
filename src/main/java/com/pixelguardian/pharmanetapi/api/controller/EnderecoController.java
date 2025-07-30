package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.EnderecoDTO;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/enderecos")
@RequiredArgsConstructor
@CrossOrigin
public class EnderecoController {

    private final EnderecoService enderecoService;

    @GetMapping()
    public ResponseEntity<List<EnderecoDTO>> get() {
        List<Endereco> enderecos = enderecoService.getEnderecos();
        return ResponseEntity.ok(enderecos.stream().map(EnderecoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> getById(@PathVariable("id") Long id) {
        return enderecoService.getEnderecoById(id)
                .map(EnderecoDTO::create)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}