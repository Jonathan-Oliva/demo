package com.example.demo.service;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.RegistroUsuarioDTO;
import com.example.demo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private RegistroUsuarioService registroUsuarioService;

    @Test
    void registrar_contrasenaEnTextoPlano_guardaSoloElHash() {
        String password = "secreto-de-prueba";
        String hash = "$2a$10$hash-de-prueba";
        when(usuarioRepository.existsByEmail("ana@example.com")).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(hash);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        registroUsuarioService.registrar(new RegistroUsuarioDTO("ana@example.com", password));

        ArgumentCaptor<Usuario> usuarioGuardado = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioGuardado.capture());
        verify(passwordEncoder).encode(eq(password));
        assertEquals(hash, usuarioGuardado.getValue().getPasswordHash());
        assertNotEquals(password, usuarioGuardado.getValue().getPasswordHash());
    }
}
