package com.example.demo.repository;

import com.example.demo.domain.Favorito;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository {
    Favorito save(Favorito favorito);
    Optional<Favorito> findById(Long id);
    List<Favorito> findAll();
    void deleteById(Long id);
    List<Favorito> findByListaId(Long listaId);
}
