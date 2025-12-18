package com.pixelguardian.pharmanetapi.service;

import com.pixelguardian.pharmanetapi.model.entity.Cargo;
import com.pixelguardian.pharmanetapi.model.entity.Endereco;
import com.pixelguardian.pharmanetapi.model.entity.Farmacia;
import com.pixelguardian.pharmanetapi.model.entity.Funcionario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FuncionarioServiceTest {

    @Autowired
    private FuncionarioService service;

    private Endereco endereco;
    private Cargo cargo;
    private Farmacia farmacia;
    private Funcionario funcionario;

    @BeforeEach
    void setup() {

        // Gerar os objetos em casos normais

        endereco = new Endereco(1L, "Fábrica", "36080-011", "Juiz de Fora", "", "R. Bernardo Mascarenhas", "1283", "MG");
        cargo = new Cargo(1L, "Farmacêutico", null);
        farmacia = new Farmacia(1L, "3123123123", "farmacianumero1@gmail.com", "Farmácia 1", "4444-4444", null);

        funcionario = new Funcionario(1L, "João", "", "senha123", "12345678900", "32900001111", "2025-12-05", endereco, 0.0f, "manha", cargo, farmacia);

    }

    @Test
    void validarNomeNulo() {

        funcionario.setNome(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void validarNomeVazio() {

        funcionario.setNome("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Nome inválido", e.getMessage());
        }
    }

    @Test
    void validarSenhaNulo() {

        funcionario.setSenha(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Senha inválida", e.getMessage());
        }
    }

    @Test
    void validarSenhaVazio() {

        funcionario.setSenha("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Senha inválida", e.getMessage());
        }
    }

    @Test
    void validarCpfNulo() {

        funcionario.setCpf(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void validarCpfVazio() {

        funcionario.setCpf("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("CPF inválido", e.getMessage());
        }
    }

    @Test
    void validarTelefoneNulo() {

        funcionario.setTelefone(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void validarTelefoneVazio() {

        funcionario.setTelefone("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Telefone inválido", e.getMessage());
        }
    }

    @Test
    void validarDataAdmissaoNulo() {

        funcionario.setDataAdmissao(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Data de Admissão inválida", e.getMessage());
        }
    }

    @Test
    void validarDataAdmissaoVazio() {

        funcionario.setDataAdmissao("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Data de Admissão inválida", e.getMessage());
        }
    }

    @Test
    void validarEnderecoNulo() {

        funcionario.setEndereco(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarEnderecoIdNulo() {

        endereco.setId(null);
        funcionario.setEndereco(endereco);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarEnderecoIdZero() {

        endereco.setId(0L);
        funcionario.setEndereco(endereco);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Endereço inválido", e.getMessage());
        }
    }

    @Test
    void validarSalarioNulo() {

        funcionario.setSalario(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Salário inválido", e.getMessage());
        }
    }

    @Test
    void validarSalarioMenorQueZero() {

        funcionario.setSalario(-0.01f);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Salário inválido", e.getMessage());
        }
    }

    @Test
    void validarExpedienteNulo() {

        funcionario.setExpediente(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Expediente inválido", e.getMessage());
        }
    }

    @Test
    void validarExpedienteVazio() {

        funcionario.setExpediente("");
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Expediente inválido", e.getMessage());
        }
    }

    @Test
    void validarCargoNulo() {

        funcionario.setCargo(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Cargo inválido", e.getMessage());
        }
    }

    @Test
    void validarCargoIdNulo() {

        cargo.setId(null);
        funcionario.setCargo(cargo);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Cargo inválido", e.getMessage());
        }
    }

    @Test
    void validarCargoIdZero() {

        cargo.setId(0L);
        funcionario.setCargo(cargo);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Cargo inválido", e.getMessage());
        }
    }

    @Test
    void validarFarmaciaNulo() {

        funcionario.setFarmacia(null);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Farmácia inválida", e.getMessage());
        }
    }

    @Test
    void validarFarmaciaIdNulo() {

        farmacia.setId(null);
        funcionario.setFarmacia(farmacia);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Farmácia inválida", e.getMessage());
        }
    }

    @Test
    void validarFarmaciaIdZero() {

        farmacia.setId(0L);
        funcionario.setFarmacia(farmacia);
        try {
            service.validar(funcionario);
            Assertions.fail("Deveria haver uma exceção");
        } catch (Exception e) {
            Assertions.assertEquals("Farmácia inválida", e.getMessage());
        }
    }

    @Test
    void validarTudoCerto(){

        try {
            service.validar(funcionario);
            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.fail("Não deveria haver uma exceção");
        }

    }
}