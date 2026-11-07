package com.example.demo.controller;

import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.ListaDTO;
import com.example.demo.dto.MoverFavoritosRequest;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.service.ListaService;
import com.example.demo.security.UsuarioPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@SecurityRequirement(name = "bearerAuth")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @Operation(summary = "Crea una lista")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListaDTO crearLista(@Valid @RequestBody NuevaListaDTO dto,
                               @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.crearLista(dto, usuario);
    }

    @Operation(summary = "Lista todas las listas")
    @GetMapping
    public List<ListaDTO> listarTodas(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.listarTodas(usuario);
    }

    @Operation(summary = "Obtiene una lista por ID")
    @GetMapping("/{id}")
    public ListaDTO obtenerPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.obtenerPorId(id, usuario);
    }

    @Operation(summary = "Obtiene los favoritos que pertenecen a una lista")
    @GetMapping("/{id}/favoritos")
    public List<FavoritoDTO> obtenerFavoritos(@PathVariable Long id,
                                              @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.obtenerFavoritosDeLista(id, usuario);
    }

    @Operation(summary = "Elimina una lista (solo si está vacía)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLista(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal usuario) {
        listaService.eliminarLista(id, usuario);
    }

    @Operation(summary = "Mueve los favoritos a otra lista y elimina la lista origen")
    @PostMapping("/{id}/mover-favoritos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moverFavoritos(@PathVariable Long id,
                               @Valid @RequestBody MoverFavoritosRequest dto,
                               @AuthenticationPrincipal UsuarioPrincipal usuario) {
        listaService.moverFavoritos(id, dto, usuario);
    }
}
