package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.CargoDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cargos")
@RequiredArgsConstructor
@CrossOrigin
public class CargoController {

    private final CargoService cargoService;

    @GetMapping("")
    public ResponseEntity get(){
        List<Cargo> cargos = cargoService.getCargos();
        return ResponseEntity.ok(cargos.stream().map(CargoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = cargoService.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(cargo.map(CargoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody CargoDTO dto) {
        try {
            Cargo cargo = converter(dto);
            cargo = cargoService.salvar(cargo);
            return new ResponseEntity(cargo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CargoDTO dto) {
        if (!cargoService.getCargoById(id).isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Cargo cargo = converter(dto);
            cargo.setId(id);
            cargoService.salvar(cargo);
            return ResponseEntity.ok(cargo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = cargoService.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            cargoService.excluir(cargo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private Cargo converter(CargoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Cargo.class);    }
}
