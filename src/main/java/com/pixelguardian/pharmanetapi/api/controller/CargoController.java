package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.CargoDTO;
import com.pixelguardian.pharmanetapi.api.dto.PermissaoDTO;
import com.pixelguardian.pharmanetapi.api.dto.PermissaoIndividualDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.model.entity.Permissao;
import com.pixelguardian.pharmanetapi.model.entity.PermissaoIndividual;
import com.pixelguardian.pharmanetapi.service.CargoService;
import com.pixelguardian.pharmanetapi.service.PermissaoIndividualService;
import com.pixelguardian.pharmanetapi.service.PermissaoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final PermissaoService permissaoService;
    private final PermissaoIndividualService permissaoIndividualService;

    @GetMapping("/permissoes")
    public ResponseEntity getPermissoes(){
        List<Permissao> permissoes = permissaoService.getPermissaos();
        return ResponseEntity.ok(permissoes.stream().map(PermissaoDTO::create).collect(Collectors.toList()));
    }


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
            ModelMapper modelMapper = new ModelMapper();
            Cargo cargo = modelMapper.map(dto, Cargo.class);
            cargo = cargoService.salvar(cargo);



            for (PermissaoIndividualDTO perm : dto.getPermissoes()){
                PermissaoIndividual permissaoIndividual = modelMapper.map(perm, PermissaoIndividual.class);

                permissaoIndividual.setCargo(cargo);
                permissaoIndividual.setTemPermissao(true);

                permissaoIndividualService.salvar(permissaoIndividual);

            }
            cargo = cargoService.salvar(cargo);
            return new ResponseEntity(cargo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody CargoDTO dto) {
        if (!cargoService.getCargoById(id).isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ModelMapper modelMapper = new ModelMapper();
            Cargo cargo = modelMapper.map(dto, Cargo.class);

            List<PermissaoIndividual> permissoesAtuais =  permissaoIndividualService.findByCargo(cargo);

            for (PermissaoIndividual perm : permissoesAtuais){

                perm.setTemPermissao(false);
                permissaoIndividualService.salvar(perm);

            }

            for (PermissaoIndividualDTO perm : dto.getPermissoes()){
                PermissaoIndividual permissaoIndividual = modelMapper.map(perm, PermissaoIndividual.class);

                permissaoIndividual.setCargo(cargo);
                permissaoIndividual.setTemPermissao(true);

                permissaoIndividual = permissaoIndividualService.salvar(permissaoIndividual);

            }
            cargoService.salvar(cargo);
            return ResponseEntity.ok(cargo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Transactional
    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Cargo> cargo = cargoService.getCargoById(id);
        if (!cargo.isPresent()) {
            return new ResponseEntity("Cargo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {

            permissaoIndividualService.deleteByCargo(cargo.get());

            cargoService.excluir(cargo.get());

            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



//    private Cargo converter(CargoDTO dto) {
//        ModelMapper modelMapper = new ModelMapper();
//        Cargo cargo = modelMapper.map(dto, Cargo.class);
//
//        for (PermissaoIndividualDTO perm : dto.getPermissoes()){
//            PermissaoIndividual permissaoIndividual = modelMapper.map(perm, PermissaoIndividual.class);
//            Optional<PermissaoIndividual> permissaoLocalizada = permissaoIndividualService.findByCargoAndPermissao(
//                    permissaoIndividual.getCargo(),
//                    permissaoIndividual.getPermissao());
//            if (!permissaoLocalizada.isPresent()){
//
//            }
//        }
//        return cargo;
//    }
}
