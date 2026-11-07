package com.example.demo.service;

import com.example.demo.domain.Favorito;
import com.example.demo.domain.Lista;
import com.example.demo.dto.FavoritoDTO;
import com.example.demo.dto.ListaDTO;
import com.example.demo.dto.MoverFavoritosRequest;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.exception.ListaNoVaciaException;
import com.example.demo.exception.RecursoNoEncontradoException;
import com.example.demo.exception.AccesoDenegadoException;
import com.example.demo.repository.FavoritoRepository;
import com.example.demo.repository.ListaRepository;
import com.example.demo.security.UsuarioPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FavoritoRepository favoritoRepository;

    public ListaService(ListaRepository listaRepository, FavoritoRepository favoritoRepository) {
        this.listaRepository = listaRepository;
        this.favoritoRepository = favoritoRepository;
    }

    public ListaDTO crearLista(NuevaListaDTO dto, UsuarioPrincipal usuario) {
        Lista nueva = new Lista(null, dto.nombre());
        nueva.setUsuarioId(usuario.id());
        return aDTO(listaRepository.save(nueva));
    }

    public List<ListaDTO> listarTodas(UsuarioPrincipal usuario) {
        List<Lista> listas = esAdmin(usuario)
                ? listaRepository.findAll()
                : listaRepository.findByUsuarioId(usuario.id());
        return listas.stream()
                .map(this::aDTO)
                .collect(Collectors.toList());
    }

    public ListaDTO obtenerPorId(Long id, UsuarioPrincipal usuario) {
        return aDTO(buscarYAutorizar(id, usuario));
    }

    public List<FavoritoDTO> obtenerFavoritosDeLista(Long id, UsuarioPrincipal usuario) {
        buscarYAutorizar(id, usuario);
        return favoritoRepository.findByListaId(id).stream()
                .map(this::aFavoritoDTO)
                .collect(Collectors.toList());
    }

    public void eliminarLista(Long id, UsuarioPrincipal usuario) {
        buscarYAutorizar(id, usuario);
        if (!favoritoRepository.findByListaId(id).isEmpty()) {
            throw new ListaNoVaciaException("La lista " + id + " todavía tiene favoritos, no se puede eliminar");
        }
        listaRepository.deleteById(id);
    }

    @Transactional
    public void moverFavoritos(Long origenId, MoverFavoritosRequest request, UsuarioPrincipal usuario) {
        Lista origen = buscarYAutorizar(origenId, usuario);
        Lista destino = buscarYAutorizar(request.listaDestinoId(), usuario);

        for (Favorito f : favoritoRepository.findByListaId(origen.getId())) {
            f.setListaId(destino.getId());
            favoritoRepository.save(f);
        }
        listaRepository.deleteById(origen.getId());
    }

    private Lista buscarOFallar(Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la lista con ID: " + id));
    }

    private Lista buscarYAutorizar(Long id, UsuarioPrincipal usuario) {
        Lista lista = buscarOFallar(id);
        if (!esAdmin(usuario) && !usuario.id().equals(lista.getUsuarioId())) {
            throw new AccesoDenegadoException("No tenés permiso para acceder a esta lista");
        }
        return lista;
    }

    private boolean esAdmin(UsuarioPrincipal usuario) {
        return "ADMIN".equals(usuario.rol());
    }

    private ListaDTO aDTO(Lista lista) {
        return new ListaDTO(lista.getId(), lista.getNombre());
    }

    private FavoritoDTO aFavoritoDTO(Favorito f) {
        return new FavoritoDTO(f.getId(), f.getIdProductoExterno(), f.getNotaPersonal(), f.getFechaAgregado(), f.getListaId());
    }
}
