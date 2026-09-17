package com.example.demo.controller;

import com.example.demo.dto.ProductoDTO;
import com.example.demo.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Lista todos los productos del catálogo externo")
    @GetMapping
    public List<ProductoDTO> listarProductos() {
        return productoService.obtenerProductos();
    }

    @Operation(summary = "Obtiene un producto específico por su ID")
    @GetMapping("/{id}")
    public ProductoDTO obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }
}
