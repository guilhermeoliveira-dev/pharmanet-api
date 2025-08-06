package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.model.entity.PedidoCompra;
import com.pixelguardian.pharmanetapi.util.RandomNumberGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PedidoCompraServiceTest {

    @Autowired
    public PedidoCompraService pedidoCompraService;

    @Test
    void testarGerarCodigosUnicos(){

        int n = 10000;

        String[] codigos = new String[n];

        for(int i = 0; i < n; i++){

            codigos[i] = pedidoCompraService.gerarCodigo();

            PedidoCompra pedido = new PedidoCompra();
            pedido.setCodigo(codigos[i]);
            pedidoCompraService.salvar(pedido);

            Assertions.assertFalse(pedidoCompraService.existsPedidoCompraByCodigo(codigos[i]));

            for (int j = 0; j < n; j++){

                if (i == j || codigos[j] != null || codigos[i] == null){
                    continue;
                }

                Assertions.assertNotEquals(codigos[j], codigos[i]);

            }
        }

    }

}
