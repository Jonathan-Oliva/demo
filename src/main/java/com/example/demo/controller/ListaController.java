package com.example.demo.controller;

import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.ListaDTO;
import com.example.demo.dto.MoverFavoritosRequest;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaController {

    private final ListaService listaService;

    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @Operation(summary = "Crea una lista")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListaDTO crearLista(@Valid @RequestBody NuevaListaDTO dto) {
        return listaService.crearLista(dto);
    }

    @Operation(summary = "Lista todas las listas")
    @GetMapping
    public List<ListaDTO> listarTodas() {
        return listaService.listarTodas();
    }

    @Operation(summary = "Obtiene una lista por ID")
    @GetMapping("/{id}")
    public ListaDTO obtenerPorId(@PathVariable Long id) {
        return listaService.obtenerPorId(id);
    }

    @Operation(summary = "Obtiene los favoritos que pertenecen a una lista")
    @GetMapping("/{id}/favoritos")
    public List<FavoritoDTO> obtenerFavoritos(@PathVariable Long id) {
        return listaService.obtenerFavoritosDeLista(id);
    }

    @Operation(summary = "Elimina una lista (solo si está vacía)")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarLista(@PathVariable Long id) {
        listaService.eliminarLista(id);
    }

    @Operation(summary = "Mueve los favoritos a otra lista y elimina la lista origen")
    @PostMapping("/{id}/mover-favoritos")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void moverFavoritos(@PathVariable Long id, @Valid @RequestBody MoverFavoritosRequest dto) {
        listaService.moverFavoritos(id, dto);
    }
}
