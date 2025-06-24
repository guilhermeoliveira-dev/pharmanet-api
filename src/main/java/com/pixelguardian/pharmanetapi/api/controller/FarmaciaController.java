package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.FarmaciaDTO;
import com.pixelguardian.pharmanetapi.api.dto.FarmaciaDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Farmacia;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.model.entity.Farmacia;
import com.pixelguardian.pharmanetapi.service.EnderecoService;
import com.pixelguardian.pharmanetapi.service.FarmaciaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/farmacias")
@RequiredArgsConstructor
@CrossOrigin
public class FarmaciaController {

    private final FarmaciaService farmaciaService;
    private final EnderecoService enderecoService;

    @GetMapping()
    public ResponseEntity get() {
        List<Farmacia> farmacias = farmaciaService.getFarmacias();
        return ResponseEntity.ok(farmacias.stream().map(FarmaciaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Farmacia> farmacia = farmaciaService.getFarmaciaById(id);
        if (!farmacia.isPresent()) {
            return new ResponseEntity("Farmacia não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(farmacia.map(FarmaciaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(FarmaciaDTO dto) {
        try {
            Farmacia farmacia = converter(dto);
            farmacia = farmaciaService.salvar(farmacia);
            return new ResponseEntity(farmacia, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, FarmaciaDTO dto) {
        if (!farmaciaService.getFarmaciaById(id).isPresent()) {
            return new ResponseEntity("Farmácia não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Farmacia farmacia = converter(dto);
            farmacia.setId(id);
            Endereco endereco = enderecoService.salvar(farmacia.getEndereco());
            farmacia.setEndereco(endereco);
            farmaciaService.salvar(farmacia);
            return ResponseEntity.ok(farmacia);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    public Farmacia converter(FarmaciaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Farmacia farmacia = modelMapper.map(dto, Farmacia.class);
        Endereco endereco = modelMapper.map(dto, Endereco.class);
        farmacia.setEndereco(endereco);
//        if (dto.getIdEndereco() != null) {
//            Optional<Endereco> endereco = enderecoService.getEnderecoById((dto.getIdEndereco()));
//            if (endereco.isPresent()) {
//                farmacia.setEndereco(endereco.get());
//            } else {
//                farmacia.setEndereco(null);
//            }
//        }
        return farmacia;
    }
}
