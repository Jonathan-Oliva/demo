package com.example.demo.controller;

import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.NuevoFavoritoDTO;
import com.example.demo.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    @Operation(summary = "Crea un nuevo favorito en el sistema")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Devuelve 201 Created
    public FavoritoDTO crearFavorito(@Valid @RequestBody NuevoFavoritoDTO nuevoFavoritoDTO) {
        return favoritoService.crearFavorito(nuevoFavoritoDTO);
    }

    @Operation(summary = "Lista todos los favoritos guardados")
    @GetMapping
    public List<FavoritoDTO> listarFavoritos() {
        return favoritoService.listarFavoritos(); // Devuelve 200 OK por defecto
    }

    @Operation(summary = "Obtiene un favorito específico por su ID")
    @GetMapping("/{id}")
    public FavoritoDTO obtenerFavorito(@PathVariable Long id) {
        return favoritoService.obtenerFavoritoPorId(id); // Devuelve 200 OK por defecto
    }

    @Operation(summary = "Actualiza los datos de un favorito existente")
    @PutMapping("/{id}")
    public FavoritoDTO actualizarFavorito(@PathVariable Long id, @Valid @RequestBody NuevoFavoritoDTO dto) {
        return favoritoService.actualizarFavorito(id, dto); // Devuelve 200 OK por defecto
    }

    @Operation(summary = "Elimina un favorito del sistema")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Devuelve 204 No Content
    public void eliminarFavorito(@PathVariable Long id) {
        favoritoService.eliminarFavorito(id);
    }
}
