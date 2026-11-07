package com.example.demo.repository;

import com.example.demo.domain.Lista;
import java.util.List;
import java.util.Optional;

public interface ListaRepository {
    Lista save(Lista lista);
    Optional<Lista> findById(Long id);
    List<Lista> findAll();
    List<Lista> findByUsuarioId(Long usuarioId);
    void deleteById(Long id);
}
