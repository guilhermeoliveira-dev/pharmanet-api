package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.PedidoCompraDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.service.EnderecoService;
import com.pixelguardian.pharmanetapi.service.PedidoCompraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        List<PedidoCompraDTO> dtos = new ArrayList<>();
        for (PedidoCompra pedidoCompra : pedidoCompras) {
            PedidoCompraDTO dto = PedidoCompraDTO.create(pedidoCompra);
            dto.setValorTotal(pedidoCompraService.calcularValorTotal(pedidoCompra));
            dtos.add(dto);
        }
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<PedidoCompra> pedidoCompra = pedidoCompraService.getPedidoCompraById(id);
        if (!pedidoCompra.isPresent()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        PedidoCompraDTO dto = PedidoCompraDTO.create(pedidoCompra.get());
        dto.setValorTotal(pedidoCompraService.calcularValorTotal(pedidoCompra.get()));
        return ResponseEntity.ok(dto);
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody PedidoCompraDTO dto) {
        try {
            PedidoCompra pedidoCompra = converter(dto);
            pedidoCompra = pedidoCompraService.salvar(pedidoCompra);
            return new ResponseEntity(pedidoCompra, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PedidoCompraDTO dto) {
        if (!pedidoCompraService.getPedidoCompraById(id).isPresent()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = converter(dto);
            pedidoCompra.setId(id);
            pedidoCompraService.salvar(pedidoCompra);
            return ResponseEntity.ok(pedidoCompra);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        try {
            pedidoCompraService.excluir(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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