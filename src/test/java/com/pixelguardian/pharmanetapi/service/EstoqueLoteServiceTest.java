package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.model.entity.Estoque;
import com.pixelguardian.pharmanetapi.model.entity.EstoqueLote;
import com.pixelguardian.pharmanetapi.model.entity.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class EstoqueLoteServiceTest {

    @Autowired
    public EstoqueLoteService estoqueLoteService;
    @Autowired
    public ProdutoService produtoService;

    @Test
    void testBuscarEstoquePorProduto(){

        Produto produto = produtoService.getProdutoById(3L).get();

        Optional<EstoqueLote> escolhido = estoqueLoteService.acharEstoquePorProduto(produto);

        System.out.println(escolhido.get());


    }


}
