package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.FuncionarioDTO;
import com.pixelguardian.pharmanetapi.api.dto.FuncionarioDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.CargoService;
import com.pixelguardian.pharmanetapi.service.EnderecoService;
import com.pixelguardian.pharmanetapi.service.FarmaciaService;
import com.pixelguardian.pharmanetapi.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
@CrossOrigin
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final EnderecoService enderecoService;
    private final CargoService cargoService;
    private final FarmaciaService farmaciaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Funcionario> funcionarios = funcionarioService.getFuncionarios();
        return ResponseEntity.ok(funcionarios.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario.map(FuncionarioDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody FuncionarioDTO dto) {
        try {
            Funcionario funcionario = converter(dto);
            Endereco endereco = enderecoService.salvar(funcionario.getEndereco());
            funcionario.setEndereco(endereco);
            funcionario = funcionarioService.salvar(funcionario);
            return new ResponseEntity(funcionario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody FuncionarioDTO dto) {
        if (!funcionarioService.getFuncionarioById(id).isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Funcionario funcionario = converter(dto);
            funcionario.setId(id);
            Endereco endereco = enderecoService.salvar(funcionario.getEndereco());
            funcionario.setEndereco(endereco);
            funcionario = funcionarioService.salvar(funcionario);
            return ResponseEntity.ok(funcionario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            funcionarioService.excluir(funcionario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Funcionario converter(FuncionarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Funcionario funcionario = modelMapper.map(dto, Funcionario.class);
        Endereco endereco = modelMapper.map(dto, Endereco.class);
        funcionario.setEndereco(endereco);
//        if (dto.getIdEndereco() != null) {
//            Optional<Endereco> endereco = enderecoService.getEnderecoById((dto.getIdEndereco()));
//            if (endereco.isPresent()) {
//                funcionario.setEndereco(endereco.get());
//            } else {
//                funcionario.setEndereco(null);
//            }
//        }
        if (dto.getIdCargo() != null) {
            Optional<Cargo> cargo = cargoService.getCargoById(dto.getIdCargo());
            if (cargo.isPresent()) {
                funcionario.setCargo(cargo.get());
            } else {
                funcionario.setCargo(null);
            }
        }
        if (dto.getIdFarmacia() != null) {
            Optional<Farmacia> farmacia = farmaciaService.getFarmaciaById(dto.getIdFarmacia());
            if (farmacia.isPresent()) {
                funcionario.setFarmacia(farmacia.get());
            } else {
                funcionario.setCargo(null);
            }
        }
        return funcionario;
    }
}
