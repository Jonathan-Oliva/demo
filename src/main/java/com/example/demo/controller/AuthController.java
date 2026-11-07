package com.example.demo.controller;

import com.example.demo.dto.RegistroUsuarioDTO;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.TokenDTO;
import com.example.demo.dto.UsuarioRegistroResponse;
import com.example.demo.service.AutenticacionService;
import com.example.demo.service.RegistroUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro de usuarios y obtención de tokens JWT")
public class AuthController {
    private final RegistroUsuarioService registroUsuarioService;
    private final AutenticacionService autenticacionService;

    public AuthController(RegistroUsuarioService registroUsuarioService, AutenticacionService autenticacionService) {
        this.registroUsuarioService = registroUsuarioService;
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registra un usuario", description = "Crea una cuenta con rol USER y contraseña hasheada. "
            + "Responde 409 si el email ya está registrado.")
    public UsuarioRegistroResponse registrar(@Valid @RequestBody RegistroUsuarioDTO dto) {
        return registroUsuarioService.registrar(dto);
    }

    @PostMapping("/login")
    @Operation(summary = "Inicia sesión", description = "Valida email y contraseña y devuelve un JWT. "
            + "Responde 401 si las credenciales son inválidas.")
    public TokenDTO login(@Valid @RequestBody LoginDTO dto) {
        return autenticacionService.login(dto);
    }
}
