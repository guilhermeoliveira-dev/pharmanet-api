package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.PagamentoDTO;
import com.pixelguardian.pharmanetapi.api.dto.ItemCarrinhoDTO;
import com.pixelguardian.pharmanetapi.api.dto.CarrinhoDTO;
import com.pixelguardian.pharmanetapi.api.dto.ItemPedidoDTO;
import com.pixelguardian.pharmanetapi.api.dto.PedidoCompraDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.*;
import com.pixelguardian.pharmanetapi.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pedidoCompras")
@RequiredArgsConstructor
@CrossOrigin
public class PedidoCompraController {

    private final PedidoCompraService pedidoCompraService;
    private final EnderecoService enderecoService;
    private final EstoqueService estoqueService;
    private final ReceitaService receitaService;
    private final ItemPedidoService itemPedidoService;
    private final UsuarioService usuarioService;
    private final EstoqueLoteService estoqueLoteService;
    private final ProdutoService produtoService;

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

    @PostMapping("/carrinho")
    public ResponseEntity criarPedidoDoCarrinho(@RequestBody CarrinhoDTO dto) {
        try {
            PedidoCompra pedidoCompra = pedidoCompraService.criarPedido(dto);
            return new ResponseEntity(PedidoCompraDTO.create(pedidoCompra), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno ao criar o pedido: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody PedidoCompraDTO dto) {
        try {
            PedidoCompra pedidoCompra = converter(dto);
            if (pedidoCompra.getCodigo() == null || pedidoCompra.getCodigo().trim().isEmpty()) {
                pedidoCompra.setCodigo(pedidoCompraService.gerarCodigo());
            }
            if (pedidoCompra.getDataCriacao() == null || pedidoCompra.getDataCriacao().trim().isEmpty()) {
                pedidoCompra.setDataCriacao(DateUtil.formatarHifenReverso(LocalDate.now()));
            }

            if (pedidoCompra.getStatus() == null || pedidoCompra.getStatus().trim().isEmpty()) {
                pedidoCompra.setStatus("pagamento pendente");
            }
            if (pedidoCompra.getStatusEntrega() == null || pedidoCompra.getStatusEntrega().trim().isEmpty()){
                pedidoCompra.setStatusEntrega("pendente");
            }

            pedidoCompra = pedidoCompraService.salvar(pedidoCompra);

            if (dto.getPedidos() != null) {
                for (ItemPedidoDTO dtoItemPedido: dto.getPedidos()){
                    ItemPedido itemPedido = converterItemPedido(dtoItemPedido, pedidoCompra);
                    itemPedidoService.salvar(itemPedido);
                }
            }
            return new ResponseEntity(PedidoCompraDTO.create(pedidoCompra), HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody PedidoCompraDTO dto) {
        if (!pedidoCompraService.getPedidoCompraById(id).isPresent()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = converter(dto);
            pedidoCompra.setId(id);
            pedidoCompraService.atualizar(pedidoCompra);

            // Lógica de atualização de itens omitida por simplicidade, foco no fluxo principal

            return ResponseEntity.ok(PedidoCompraDTO.create(pedidoCompra));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // MÉTODO CORRIGIDO: Agora chama o método implementado no Service (Issue 1)
    @PutMapping("/confirmar_entrega/{id}")
    public ResponseEntity confirmarEntrega(@PathVariable("id") Long id) {
        if (pedidoCompraService.getPedidoCompraById(id).isEmpty()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = pedidoCompraService.confirmarEntrega(id);
            return ResponseEntity.ok(PedidoCompraDTO.create(pedidoCompra));
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/confirmar_pagamento/{id}")
    public ResponseEntity confirmarPagamento(@PathVariable("id") Long id, @RequestBody PagamentoDTO dto) {
        if (pedidoCompraService.getPedidoCompraById(id).isEmpty()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = pedidoCompraService.confirmarPagamento(id, dto);
            return ResponseEntity.ok(PedidoCompraDTO.create(pedidoCompra));
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
        PropertyMap<PedidoCompraDTO, PedidoCompra> pedidoCompraMap = new PropertyMap<>() {
            @Override protected void configure() {skip(destination.getUsuario()); skip(destination.getEndereco());}
        };
        modelMapper.addMappings(pedidoCompraMap);

        PedidoCompra pedidoCompra = modelMapper.map(dto, PedidoCompra.class);

        if (dto.getIdUsuario() != null){
            Usuario usuario = usuarioService.getUsuarioById(dto.getIdUsuario())
                    .orElseThrow(() -> new RegraNegocioException("Usuário associado ao pedido não encontrado."));
            pedidoCompra.setUsuario(usuario);

            if (dto.getIdEndereco() == null && usuario.getEndereco() != null) {
                pedidoCompra.setEndereco(usuario.getEndereco());
            }
        }

        if (dto.getIdEndereco() != null){
            Endereco endereco = enderecoService.getEnderecoById(dto.getIdEndereco())
                    .orElseThrow(() -> new RegraNegocioException("Endereço associado ao pedido não encontrado."));
            pedidoCompra.setEndereco(endereco);
        }

        return pedidoCompra;
    }

    public ItemPedido converterItemPedido(ItemPedidoDTO dto, PedidoCompra pedidoCompra) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedido itemPedido = modelMapper.map(dto, ItemPedido.class);

        if (dto.getIdReceita() != null && dto.getIdReceita() != 0) {
            Receita receita = receitaService.getReceitaById(dto.getIdReceita())
                    .orElseThrow(() -> new RegraNegocioException("Receita associada ao item do pedido não encontrada."));
            itemPedido.setReceita(receita);
        } else {
            itemPedido.setReceita(null);
        }

        if (dto.getIdEstoque() != null && dto.getIdEstoque() != 0) {
            Estoque estoque = estoqueService.getEstoqueById(dto.getIdEstoque())
                    .orElseThrow(() -> new RegraNegocioException("Estoque associado ao item do pedido não encontrado."));
            itemPedido.setEstoque(estoque);
        } else {
            itemPedido.setEstoque(null);
        }

        itemPedido.setPedidoCompra(pedidoCompra);
        return itemPedido;
    }
}