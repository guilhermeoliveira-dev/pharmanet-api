package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.FeedbackDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.Cliente;
import com.pixelguardian.pharmanetapi.model.entity.Feedback;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import com.pixelguardian.pharmanetapi.service.ClienteService;
import com.pixelguardian.pharmanetapi.service.FeedbackService;
import com.pixelguardian.pharmanetapi.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/feedbacks")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;

    @GetMapping("")
    public ResponseEntity get() {
        List<Feedback> feedbacks = feedbackService.getFeedbacks();
        return ResponseEntity.ok(feedbacks.stream().map(FeedbackDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (!feedback.isPresent()) {
            return new ResponseEntity("Feedback não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(feedback.map(FeedbackDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(FeedbackDTO dto) {
        try {
            Feedback feedback = converter(dto);
            feedback = feedbackService.salvar(feedback);
            return new ResponseEntity(feedback, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, FeedbackDTO dto) {
        if (!feedbackService.getFeedbackById(id).isPresent()) {
            return new ResponseEntity("Feedback não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Feedback feedback = converter(dto);
            feedback.setId(id);
            feedbackService.salvar(feedback);
            return ResponseEntity.ok(feedback);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        if (!feedback.isPresent()) {
            return new ResponseEntity("Feedback não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            feedbackService.excluir(feedback.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Feedback converter(FeedbackDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Feedback feedback = modelMapper.map(dto, Feedback.class);
        if (dto.getIdCliente() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdCliente());
            if (cliente.isPresent()) {
                feedback.setCliente(cliente.get());
            } else {
                feedback.setCliente(null);
            }
        }
        if (dto.getIdProduto() != null) {
            Optional<Produto> produto = produtoService.getProdutoById(dto.getIdProduto());
            if (produto.isPresent()) {
                feedback.setProduto(produto.get());
            } else {
                feedback.setProduto(null);
            }
        }
        return feedback;
    }
}
