package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.NotificacaoDTO;
import com.pixelguardian.pharmanetapi.exception.RegraNegocioException;
import com.pixelguardian.pharmanetapi.model.entity.*;
import com.pixelguardian.pharmanetapi.service.ClienteService;
import com.pixelguardian.pharmanetapi.service.FuncionarioService;
import com.pixelguardian.pharmanetapi.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/notificacoes")
@RequiredArgsConstructor
@CrossOrigin
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService;

    @GetMapping("")
    public ResponseEntity get() {
        List<Notificacao> notificacaos = notificacaoService.getNotificacoes();
        return ResponseEntity.ok(notificacaos.stream().map(NotificacaoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Notificacao> notificacao = notificacaoService.getNotificacaoById(id);
        if (!notificacao.isPresent()) {
            return new ResponseEntity("Notificacao não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(notificacao.map(NotificacaoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(NotificacaoDTO dto) {
        try {
            Notificacao notificacao = converter(dto);
            notificacao = notificacaoService.salvar(notificacao);
            return new ResponseEntity(notificacao, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, NotificacaoDTO dto) {
        if (!notificacaoService.getNotificacaoById(id).isPresent()) {
            return new ResponseEntity("Notificacao não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Notificacao notificacao = converter(dto);
            notificacao.setId(id);
            notificacaoService.salvar(notificacao);
            return ResponseEntity.ok(notificacao);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity excluir(@PathVariable("id") Long id) {
        Optional<Notificacao> notificacao = notificacaoService.getNotificacaoById(id);
        if (!notificacao.isPresent()) {
            return new ResponseEntity("Notificacao não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            notificacaoService.excluir(notificacao.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Notificacao converter(NotificacaoDTO dto) {
//        ModelMapper modelMapper = new ModelMapper();
//        Notificacao notificacao = modelMapper.map(dto, Notificacao.class);
        Notificacao notificacao = new Notificacao();
        notificacao.setId(dto.getId());
        notificacao.setMensagem(dto.getMensagem());
        notificacao.setDataEnvio(dto.getDataEnvio());
        notificacao.setTipoNotificacao(dto.getTipoNotificacao());
        if (dto.getIdUsuario() != null) {
            Optional<Cliente> cliente = clienteService.getClienteById(dto.getIdUsuario());
            if (cliente.isPresent()) {
                notificacao.setUsuario(cliente.get());
            } else {
                Optional<Funcionario> funcionario = funcionarioService.getFuncionarioById(dto.getIdUsuario());
                if (funcionario.isPresent()) {
                    notificacao.setUsuario(funcionario.get());
                } else {
                    notificacao.setUsuario(null);
                }
            }
        }
        return notificacao;
    }
}
