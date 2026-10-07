package com.example.demo.repository;

import com.example.demo.domain.Favorito;
import com.example.demo.entity.FavoritoEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FavoritoRepositoryAdapter implements FavoritoRepository {

    private final FavoritoJpaRepository jpaRepository;

    public FavoritoRepositoryAdapter(FavoritoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Favorito save(Favorito favorito) {
        FavoritoEntity entity = new FavoritoEntity();
        // Si el id viene, se lo seteamos (para updates). Si no, Hibernate lo ignorará o lo creará.
        entity.setId(favorito.getId());
        entity.setProductoId(favorito.getIdProductoExterno());
        entity.setNota(favorito.getNotaPersonal());
        // Convertimos el LocalDate del dominio a LocalDateTime para la base de datos
        entity.setFechaAlta(favorito.getFechaAgregado().atStartOfDay());

        FavoritoEntity savedEntity = jpaRepository.save(entity);
        return aDominio(savedEntity);
    }

    @Override
    public Optional<Favorito> findById(Long id) {
        return jpaRepository.findById(id).map(this::aDominio);
    }

    @Override
    public List<Favorito> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::aDominio)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private Favorito aDominio(FavoritoEntity entity) {
        Favorito favorito = new Favorito();
        favorito.setId(entity.getId());
        favorito.setIdProductoExterno(entity.getProductoId());
        favorito.setNotaPersonal(entity.getNota());
        // Convertimos el LocalDateTime de la base de datos a LocalDate para el dominio
        favorito.setFechaAgregado(entity.getFechaAlta().toLocalDate());
        return favorito;
    }
}
