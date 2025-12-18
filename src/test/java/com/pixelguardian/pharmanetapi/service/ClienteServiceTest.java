package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.model.entity.Cliente;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ClienteServiceTest {

    @Autowired
    private ClienteService service;

    private Endereco endereco;
    private Cliente cliente;

    @BeforeEach
    void setup() {

        // Gerar os objetos em casos normais

        endereco = new Endereco(1L, "Fábrica", "36080-011", "Juiz de Fora", "", "R. Bernardo Mascarenhas", "1283", "MG");
        cliente = new Cliente(1L, "João", "", "senha123", "12345678900", "32900001111", "2025-12-05", endereco, 0.0f);

    }

    @Test
    void validarNomeNulo() {

        cliente.setNome(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void validarNomeVazio() {

        cliente.setNome("");
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void validarSenhaNulo() {

        cliente.setSenha(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Senha inválida", e.getMessage());
        }
    }

    @Test
    void validarSenhaVazio() {

        cliente.setSenha("");
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Senha inválida", e.getMessage());
        }
    }

    @Test
    void validarCpfNulo() {

        cliente.setCpf(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void validarCpfVazio() {

        cliente.setCpf("");
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void validarTelefoneNulo() {

        cliente.setTelefone(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void validarTelefoneVazio() {

        cliente.setTelefone("");
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void validarDataAdmissaoNulo() {

        cliente.setDataAdmissao(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Data de Admissão inválida", e.getMessage());
        }
    }

    @Test
    void validarDataAdmissaoVazio() {

        cliente.setDataAdmissao("");
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Data de Admissão inválida", e.getMessage());
        }
    }

    @Test
    void validarEnderecoNulo() {

        cliente.setEndereco(null);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarEnderecoIdNulo() {

        endereco.setId(null);
        cliente.setEndereco(endereco);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarEnderecoIdZero() {

        endereco.setId(0L);
        cliente.setEndereco(endereco);
        try {
            service.validar(cliente);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarTudoCerto(){

        try {
            service.validar(cliente);
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail("Não deveria haver uma exceção");
        }

    }

}