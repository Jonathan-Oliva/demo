package com.example.demo.service;

import com.example.demo.domain.Usuario;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.TokenDTO;
import com.example.demo.exception.CredencialesInvalidasException;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AutenticacionService(UsuarioRepository usuarioRepository,
                                PasswordEncoder passwordEncoder,
                                JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public TokenDTO login(LoginDTO dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(CredencialesInvalidasException::new);
        if (!passwordEncoder.matches(dto.password(), usuario.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }
        return new TokenDTO(jwtService.emitir(usuario));
    }
}
