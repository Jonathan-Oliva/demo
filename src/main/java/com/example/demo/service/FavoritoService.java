package com.example.demo.service;

import com.example.demo.domain.Favorito;
import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.NuevoFavoritoDTO;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.repository.FavoritoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository) {
        this.favoritoRepository = favoritoRepository;
    }

    // --- CREAR ---
    public FavoritoDTO crearFavorito(NuevoFavoritoDTO nuevoFavoritoDTO) {
        Favorito entidad = mapearAEntidad(nuevoFavoritoDTO);
        Favorito entidadGuardada = favoritoRepository.save(entidad);
        return mapearADTO(entidadGuardada);
    }

    // --- LISTAR TODOS ---
    public List<FavoritoDTO> listarFavoritos() {
        return favoritoRepository.findAll().stream()
                .map(this::mapearADTO)
                .toList();
    }

    // --- OBTENER UNO ---
    public FavoritoDTO obtenerFavoritoPorId(Long id) {
        Favorito entidad = favoritoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el favorito con ID: " + id));
        return mapearADTO(entidad);
    }

    // --- ACTUALIZAR ---
    public FavoritoDTO actualizarFavorito(Long id, NuevoFavoritoDTO dto) {
        // 1. Buscamos si existe
        Favorito entidadExistente = favoritoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el favorito con ID: " + id));
        
        // 2. Actualizamos solo los datos permitidos
        entidadExistente.setIdProductoExterno(dto.idProductoExterno());
        entidadExistente.setNotaPersonal(dto.notaPersonal());
        entidadExistente.setListaId(dto.listaId());
        // No actualizamos el ID ni la fecha original
        
        // 3. Guardamos los cambios
        Favorito entidadGuardada = favoritoRepository.save(entidadExistente);
        return mapearADTO(entidadGuardada);
    }

    // --- ELIMINAR ---
    public void eliminarFavorito(Long id) {
        // Primero verificamos que exista para lanzar el 404 si es necesario
        favoritoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró el favorito con ID: " + id));
                
        favoritoRepository.deleteById(id);
    }

    // --- MÉTODOS DE MAPEO A MANO ---
    private Favorito mapearAEntidad(NuevoFavoritoDTO dto) {
        Favorito favorito = new Favorito();
        favorito.setIdProductoExterno(dto.idProductoExterno());
        favorito.setNotaPersonal(dto.notaPersonal());
        favorito.setFechaAgregado(LocalDate.now());
        favorito.setListaId(dto.listaId());
        return favorito;
    }

    private FavoritoDTO mapearADTO(Favorito entidad) {
        return new FavoritoDTO(
                entidad.getId(),
                entidad.getIdProductoExterno(),
                entidad.getNotaPersonal(),
                entidad.getFechaAgregado(),
                entidad.getListaId()
        );
    }
}
