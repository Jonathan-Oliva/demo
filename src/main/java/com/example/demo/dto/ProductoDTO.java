package com.example.demo.dto;

public record ProductoDTO(
    Long id,
    String nombre,
    String descripcion,
    double precio,
    String categoria
) {}
