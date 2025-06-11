package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.PedidoCompraDTO;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.service.EnderecoService;
import com.pixelguardian.pharmanetapi.service.PedidoCompraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidoCompras")
@RequiredArgsConstructor
@CrossOrigin
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;
    private final EnderecoService enderecoService;

    @GetMapping()
    public ResponseEntity get() {
        List<PedidoCompra> pedidoCompras = pedidoCompraService.getPedidoCompras();
        return ResponseEntity.ok(pedidoCompras.stream().map(PedidoCompraDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<PedidoCompra> pedidoCompra = pedidoCompraService.getPedidoCompraById(id);
        if (!pedidoCompra.isPresent()) {
            return new ResponseEntity("PedidoCompra não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(pedidoCompra.map(PedidoCompraDTO::create));
    }

    public PedidoCompra converter(PedidoCompraDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        PedidoCompra pedidoCompra = modelMapper.map(dto, PedidoCompra.class);
        if (dto.getIdEndereco() != null) {
            Optional<Endereco> endereco = enderecoService.getEnderecoById(dto.getIdEndereco());
            if (endereco.isPresent()) {
                pedidoCompra.setEndereco(endereco.get());
            } else {
                pedidoCompra.setEndereco(null);
            }
        }
        return pedidoCompra;
    }
}
