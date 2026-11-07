package com.example.demo.service;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.RegistroUsuarioDTO;
import com.example.demo.dto.UsuarioRegistroResponse;
import com.example.demo.exception.EmailYaRegistradoException;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistroUsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistroUsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioRegistroResponse registrar(RegistroUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new EmailYaRegistradoException(dto.email());
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setPasswordHash(passwordEncoder.encode(dto.password()));
        usuario.setRol("USER");

        try {
            Usuario guardado = usuarioRepository.save(usuario);
            return new UsuarioRegistroResponse(guardado.getId(), guardado.getEmail(), guardado.getRol());
        } catch (DataIntegrityViolationException ex) {
            // La restricción única también cubre dos registros simultáneos del mismo email.
            if (usuarioRepository.existsByEmail(dto.email())) {
                throw new EmailYaRegistradoException(dto.email());
            }
            throw ex;
        }
    }
}
