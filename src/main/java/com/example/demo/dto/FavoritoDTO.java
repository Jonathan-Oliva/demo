package com.example.demo.dto;

import java.time.LocalDate;

public record FavoritoDTO(
    Long id,
    Long idProductoExterno,
    String notaPersonal,
    LocalDate fechaAgregado,
    Long listaId
) {}
