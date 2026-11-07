package com.example.demo.service;

import com.example.demo.domain.Lista;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.repository.ListaRepository;
import com.example.demo.security.UsuarioPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListaServiceImplTest {
    @Mock
    private ListaRepository listaRepository;
    @Mock
    private FavoritoRepository favoritoRepository;
    @InjectMocks
    private ListaService listaService;

    @Test
    void crearLista_usuarioAutenticado_asociaElUsuario() {
        UsuarioPrincipal usuario = new UsuarioPrincipal(42L, "USER");
        when(listaRepository.save(any(Lista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        listaService.crearLista(new NuevaListaDTO("Lecturas"), usuario);

        ArgumentCaptor<Lista> listaGuardada = ArgumentCaptor.forClass(Lista.class);
        verify(listaRepository).save(listaGuardada.capture());
        assertEquals(42L, listaGuardada.getValue().getUsuarioId());
    }
}
