package com.pixelguardian.pharmanetapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.pixelguardian.pharmanetapi.model.entity.Usuario;
import com.pixelguardian.pharmanetapi.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarUsuarioComSucesso() {

        Usuario usuario = new Usuario() {
            @Override
            public List<String> getRoles() {
                return new ArrayList<>(Collections.singleton("USER"));
            }
        };
        usuario.setEmail("teste@pharmanet.com");
        usuario.setSenha("123456");

        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = service.salvar(usuario);

        assertNotNull(resultado, "O resultado não deveria ser nulo");
        assertEquals("teste@pharmanet.com", resultado.getEmail());

        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    public void deveVerificarInteracaoComMocks() {
        Usuario usuario = new Usuario() {
            @Override
            public List<String> getRoles() { return new ArrayList<>(); }
        };

        service.salvar(usuario);

        verify(repository).save(usuario);

        assertNotNull(repository);
    }
}