package com.pixelguardian.pharmanetapi.api.controller;

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

//    @PostMapping()
//    public ResponseEntity post(@RequestBody PedidoCompraDTO dto) {
//        try {
//            PedidoCompra pedidoCompra = converter(dto);
//            pedidoCompra = pedidoCompraService.salvar(pedidoCompra);
//            for (ItemPedidoDTO dtoItemPedido: dto.getPedidos()){
//                itemPedidoService.salvar(converterItemPedido(dtoItemPedido, pedidoCompra));
//            }
//            return new ResponseEntity(pedidoCompra, HttpStatus.CREATED);
//        } catch (RegraNegocioException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping()
    public ResponseEntity criarPedido(@RequestBody PedidoCompraDTO dto) {
        try {
            PedidoCompra pedidoCompra = converter(dto);
            pedidoCompra.setCodigo(pedidoCompraService.gerarCodigo());
            pedidoCompra.setDataCriacao(DateUtil.formatarHifenReverso(LocalDate.now()));
            pedidoCompra.setStatus("pagamento pendente");
            if (pedidoCompra.getTipoEntrega().equals("delivery") || pedidoCompra.getTipoEntrega().equals("busca no estabelecimento")){
                pedidoCompra.setStatusEntrega("pendente");
            }

            pedidoCompra = pedidoCompraService.salvar(pedidoCompra);


            for (ItemPedidoDTO dtoItemPedido: dto.getPedidos()){
                ItemPedido itemPedido = converterItemPedido(dtoItemPedido, pedidoCompra);
                Estoque estoqueEncontrado;
                Optional<Produto> produto = produtoService.getProdutoById(dtoItemPedido.getIdProduto());
                if(produto.isPresent()){
                    if(produto.get().getRequerLote()){
                        estoqueEncontrado = estoqueLoteService.acharEstoquePorProduto(produto.get()).get();
                    }else{
                        estoqueEncontrado = estoqueService.findEstoqueByProduto(produto.get()).get(1);
                    }
                }
                else{
                    estoqueEncontrado = null;
                }

                itemPedido.setEstoque(estoqueEncontrado);
                itemPedidoService.salvar(itemPedido);
            }
            return new ResponseEntity(pedidoCompra, HttpStatus.CREATED);
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
            pedidoCompraService.salvar(pedidoCompra);
            return ResponseEntity.ok(pedidoCompra);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/confirmar_entrega/{id}")
    public ResponseEntity confirmarEntrega(@PathVariable("id") Long id, @RequestBody PedidoCompraDTO dto) {
        if (pedidoCompraService.getPedidoCompraById(id).isEmpty()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = pedidoCompraService.getPedidoCompraById(id).get();

            if(pedidoCompra.getStatus().equals("entrega pendente") && pedidoCompra.getStatusEntrega().equals("pendente")){
                pedidoCompra.setStatusEntrega("entregue");
                pedidoCompra.setStatus("finalizado");
                pedidoCompra.setDataEntrega(DateUtil.formatarHifenReverso(LocalDate.now()));
            }
            pedidoCompraService.salvar(pedidoCompra);
            return ResponseEntity.ok(pedidoCompra);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/confirmar_pagamento/{id}")
    public ResponseEntity confirmarPagamento(@PathVariable("id") Long id, @RequestBody PedidoCompraDTO dto) {
        if (pedidoCompraService.getPedidoCompraById(id).isEmpty()) {
            return new ResponseEntity("Pedido de compra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            PedidoCompra pedidoCompra = pedidoCompraService.getPedidoCompraById(id).get();


            if(pedidoCompra.getStatus().equals("pagamento pendente")){
                pedidoCompra.setStatusEntrega("pendente");
                pedidoCompra.setStatus("entrega pendente");



            }

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
        PropertyMap<PedidoCompraDTO, PedidoCompra> pedidoCompraMap = new PropertyMap<>() {
            @Override protected void configure() {skip(destination.getUsuario()); skip(destination.getEndereco());}
        };
        modelMapper.addMappings(pedidoCompraMap);

        PedidoCompra pedidoCompra = modelMapper.map(dto, PedidoCompra.class);
//        if (dto.getIdEndereco() != null) {
//            Optional<Endereco> endereco = enderecoService.getEnderecoById(dto.getIdEndereco());
//            if (endereco.isPresent()) {
//                pedidoCompra.setEndereco(endereco.get());
//            } else {
//                pedidoCompra.setEndereco(null);
//            }
//        }
        if (dto.getIdUsuario() != null){
            Optional<Usuario> usuario = usuarioService.getUsuarioById(dto.getIdUsuario());
            if(usuario.isPresent()){
                pedidoCompra.setUsuario(usuario.get());
            }else{
                pedidoCompra.setUsuario(null);
            }
        }
        pedidoCompra.setEndereco(pedidoCompra.getUsuario().getEndereco());

        return pedidoCompra;
    }

    public ItemPedido converterItemPedido(ItemPedidoDTO dto, PedidoCompra pedidoCompra) {
        ModelMapper modelMapper = new ModelMapper();
        ItemPedido itemPedido = modelMapper.map(dto, ItemPedido.class);
        if (dto.getIdReceita() != null && dto.getIdReceita() != 0) {
            Optional<Receita> receita = receitaService.getReceitaById(dto.getIdReceita());
            if (receita.isPresent()) {
                itemPedido.setReceita(receita.get());
            } else {
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
        itemPedido.setPedidoCompra(pedidoCompra);
        return itemPedido;

    }


}