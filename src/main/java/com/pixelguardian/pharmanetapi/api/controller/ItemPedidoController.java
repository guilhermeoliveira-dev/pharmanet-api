package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.ItemPedidoDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.EstoqueService;
import com.pixelguardian.pharmanetapi.service.ItemPedidoService;
import com.pixelguardian.pharmanetapi.service.PedidoCompraService;
import com.pixelguardian.pharmanetapi.service.ReceitaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/itemsPedidos")
@RequiredArgsConstructor
@CrossOrigin
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;
    private final EstoqueService estoqueService;
    private final PedidoCompraService pedidoCompraService;
    private final ReceitaService receitaService;

    @GetMapping()
    public ResponseEntity get() {
        List<ItemPedido> itemPedidos = itemPedidoService.getItemPedidos();
        return ResponseEntity.ok(itemPedidos.stream().map(ItemPedidoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<ItemPedido> itemPedido = itemPedidoService.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("Item Pedido não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(itemPedido.map(ItemPedidoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(ItemPedidoDTO dto) {
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido = itemPedidoService.salvar(itemPedido);
            return new ResponseEntity(itemPedido, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, ItemPedidoDTO dto) {
        if (!itemPedidoService.getItemPedidoById(id).isPresent()) {
            return new ResponseEntity("ItemPedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            ItemPedido itemPedido = converter(dto);
            itemPedido.setId(id);
            itemPedidoService.salvar(itemPedido);
            return ResponseEntity.ok(itemPedido);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<ItemPedido> itemPedido = itemPedidoService.getItemPedidoById(id);
        if (!itemPedido.isPresent()) {
            return new ResponseEntity("ItemPedido não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            itemPedidoService.excluir(itemPedido.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ItemPedido converter(ItemPedidoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedido itemPedido = modelMapper.map(dto, ItemPedido.class);

        if (dto.getIdReceita() != null && dto.getIdReceita() != 0){
            Optional<Receita> receita = receitaService.getReceitaById(dto.getIdReceita());
            if (receita.isPresent()){
                itemPedido.setReceita(receita.get());
            }
            else{
                itemPedido.setReceita(null);
            }
        }

        if (dto.getIdEstoque() != null && dto.getIdEstoque() != 0) {
            Optional<Estoque> estoque = estoqueService.getEstoqueById((dto.getIdEstoque()));
            if (estoque.isPresent()) {
                itemPedido.setEstoque(estoque.get());
            } else {
                itemPedido.setEstoque(null);
            }
        }
        if (dto.getIdPedidoCompra() != null && dto.getIdPedidoCompra() != 0) {
            Optional<PedidoCompra> pedidoCompra = pedidoCompraService.getPedidoCompraById((dto.getIdPedidoCompra()));
            if (pedidoCompra.isPresent()) {
                itemPedido.setPedidoCompra(pedidoCompra.get());
            } else {
                itemPedido.setPedidoCompra(null);
            }
        }

        return itemPedido;
    }
}
