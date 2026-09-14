package com.example.demo.service;

import com.example.demo.client.dummyjson.DummyJsonProducto;
import com.example.demo.client.dummyjson.DummyJsonProductosResponse;
import com.example.demo.dto.ProductoDTO;
import com.example.demo.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class ProductoService {

    private final RestClient restClient;

    public ProductoService(RestClient dummyJsonRestClient) {
        this.restClient = dummyJsonRestClient;
    }

    public List<ProductoDTO> obtenerProductos() {
        DummyJsonProductosResponse response = restClient.get()
                .uri("/products") 
                .retrieve()
                .body(DummyJsonProductosResponse.class);

        if (response != null && response.products() != null) {
            return response.products().stream()
                    .map(this::mapearADTO)
                    .toList();
        }
        return List.of();
    }

    public ProductoDTO obtenerProductoPorId(Long id) {
        try {
            DummyJsonProducto externo = restClient.get()
                    .uri("/products/{id}", id)
                    .retrieve()
                    .body(DummyJsonProducto.class);
                    
            return mapearADTO(externo);
            
        } catch (HttpClientErrorException.NotFound e) {
            throw new RecursoNoEncontradoException("No se encontró el producto con ID: " + id);
        }
    }

    private ProductoDTO mapearADTO(DummyJsonProducto externo) {
        return new ProductoDTO(
                externo.id(),
                externo.title(), 
                externo.description(),
                externo.price(),
                externo.category()
        );
    }
}
