package com.example.demo.repository;

import com.example.demo.domain.Usuario;
import com.example.demo.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryAdapter implements UsuarioRepository {
    private final UsuarioJpaRepository jpaRepository;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setEmail(usuario.getEmail());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setRol(usuario.getRol());
        return aDominio(jpaRepository.save(entity));
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::aDominio);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private Usuario aDominio(UsuarioEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setEmail(entity.getEmail());
        usuario.setPasswordHash(entity.getPasswordHash());
        usuario.setRol(entity.getRol());
        return usuario;
    }
}
