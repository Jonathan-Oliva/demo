package com.example.demo.repository;

import com.example.demo.domain.Favorito;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FavoritoRepositoryMemoriaImpl implements FavoritoRepository {
    
    private final Map<Long, Favorito> tablaFavoritos = new ConcurrentHashMap<>();
    private final AtomicLong generadorDeIds = new AtomicLong(1);

    @Override
    public Favorito save(Favorito favorito) {
        if (favorito.getId() == null) {
            favorito.setId(generadorDeIds.getAndIncrement());
        }
        tablaFavoritos.put(favorito.getId(), favorito);
        return favorito;
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return Optional.ofNullable(tablaFavoritos.get(id));
    }

    @Override
    public List<Favorito> findAll() {
        return new ArrayList<>(tablaFavoritos.values());
    }

    @Override
    public void deleteById(Long id) {
        tablaFavoritos.remove(id);
    }
}
