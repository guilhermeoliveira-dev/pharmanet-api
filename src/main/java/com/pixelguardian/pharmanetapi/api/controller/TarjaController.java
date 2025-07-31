package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.ProdutoDTO;
import com.pixelguardian.pharmanetapi.api.dto.TarjaDTO;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import com.pixelguardian.pharmanetapi.model.entity.Tarja;
import com.pixelguardian.pharmanetapi.service.TarjaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tarjas")
@RequiredArgsConstructor
@CrossOrigin
public class TarjaController {

    private final TarjaService tarjaService;

    @GetMapping()
    public ResponseEntity get() {
        List<Tarja> tarjas = tarjaService.getTarjas();
        return ResponseEntity.ok(tarjas.stream().map(TarjaDTO::create).collect(Collectors.toList()));
    }

}
