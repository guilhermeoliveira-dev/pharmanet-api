package com.pixelguardian.pharmanetapi.api.controller;

import com.pixelguardian.pharmanetapi.api.dto.ItemPedidoDTO;
import com.pixelguardian.pharmanetapi.api.dto.PedidoCompraDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.RestDocsWebTestClientConfigurationCustomizer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoCompraControllerTest {

    final String BASE_URL = "http://localhost:8080/";

    @Autowired
    private PedidoCompraController controller;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void post() {

        List<ItemPedidoDTO> itens = new ArrayList<>();

        for (int i = 0; i < 3; i++){

            ItemPedidoDTO itemAtual = new ItemPedidoDTO();

            itemAtual.setId(i+1L);
            itemAtual.setQuantidade((i+1) * 2);
            itemAtual.setPrecoUnitario((i+1) * 20.0f);

            itens.add(itemAtual);
        }


//        PedidoCompraDTO dto = new PedidoCompraDTO(1, "codigo", "2025-12-25", "status", 10, "delivery", "pendente", "", 1, itens);

        PedidoCompraDTO dto = new PedidoCompraDTO();

        dto.setId(1L);
        dto.setCodigo("codigo");
        dto.setDataCriacao("2025-12-25");
        dto.setStatus("status");
        dto.setValorTotal(10.0f);
        dto.setTipoEntrega("delivery");
        dto.setStatusEntrega("pendente");
        dto.setDataEntrega("");

        dto.setIdEndereco(1L);
        dto.setUf("MG");
        dto.setCidade("Juiz de Fora");
        dto.setCep("36080-001");
        dto.setLogradouro("R. Bernardo Mascarenhas");
        dto.setNumero("1283");
        dto.setBairro("Fábrica");
        dto.setComplemento("");

        dto.setIdUsuario(1L);

        dto.setPedidos(itens);




    }
}