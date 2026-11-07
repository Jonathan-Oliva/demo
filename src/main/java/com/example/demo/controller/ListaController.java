package com.example.demo.controller;

import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.ListaDTO;
import com.example.demo.dto.MoverFavoritosRequest;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.service.ListaService;
import com.example.demo.security.UsuarioPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Listas", description = "Todas las operaciones requieren Bearer JWT. "
        + "Cada usuario accede a sus listas; ADMIN puede acceder a todas.")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @Operation(summary = "Crea una lista",
            description = "Requiere Bearer JWT. El usuario autenticado queda como dueño de la lista.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListaDTO crearLista(@Valid @RequestBody NuevaListaDTO dto,
                               @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.crearLista(dto, usuario);
    }

    @Operation(summary = "Lista todas las listas",
            description = "Requiere Bearer JWT. Devuelve las listas propias; ADMIN obtiene todas.")
    @GetMapping
    public List<ListaDTO> listarTodas(@AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.listarTodas(usuario);
    }

    @Operation(summary = "Obtiene una lista por ID",
            description = "Requiere Bearer JWT. Solo el dueño o ADMIN puede acceder; si no, responde 403.")
    @GetMapping("/{id}")
    public ListaDTO obtenerPorId(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.obtenerPorId(id, usuario);
    }

    @Operation(summary = "Obtiene los favoritos que pertenecen a una lista",
            description = "Requiere Bearer JWT. Solo el dueño o ADMIN puede acceder; si no, responde 403.")
    @GetMapping("/{id}/favoritos")
    public List<FavoritoDTO> obtenerFavoritos(@PathVariable Long id,
                                              @AuthenticationPrincipal UsuarioPrincipal usuario) {
        return listaService.obtenerFavoritosDeLista(id, usuario);
    }

    @Operation(summary = "Elimina una lista (solo si está vacía)",
            description = "Requiere Bearer JWT. Solo el dueño o ADMIN puede eliminarla; si no, responde 403.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLista(@PathVariable Long id, @AuthenticationPrincipal UsuarioPrincipal usuario) {
        listaService.eliminarLista(id, usuario);
    }

    @Operation(summary = "Mueve los favoritos a otra lista y elimina la lista origen",
            description = "Requiere Bearer JWT y ser dueño de la lista origen, salvo ADMIN; si no, responde 403.")
    @PostMapping("/{id}/mover-favoritos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moverFavoritos(@PathVariable Long id,
                               @Valid @RequestBody MoverFavoritosRequest dto,
                               @AuthenticationPrincipal UsuarioPrincipal usuario) {
        listaService.moverFavoritos(id, dto, usuario);
    }
}
