package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NuevoFavoritoDTO(
    @NotNull(message = "El ID del producto externo es obligatorio")
    Long idProductoExterno,
    
    @NotBlank(message = "La nota personal no puede estar vacía")
    String notaPersonal,

    @NotNull(message = "El ID de la lista es obligatorio")
    Long listaId
) {}
