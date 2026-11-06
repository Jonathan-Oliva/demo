package com.example.demo.security;

import com.example.demo.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final long TOKEN_LIFETIME_MILLIS = 60 * 60 * 1000;
    private final SecretKey signingKey;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String emitir(Usuario usuario) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .claim("rol", usuario.getRol())
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plusMillis(TOKEN_LIFETIME_MILLIS)))
                .signWith(signingKey)
                .compact();
    }

    public UsuarioPrincipal validar(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        Long id = Long.valueOf(claims.getSubject());
        String rol = claims.get("rol", String.class);
        if (rol == null || rol.isBlank()) {
            throw new IllegalArgumentException("El token no contiene rol");
        }
        return new UsuarioPrincipal(id, rol);
    }
}
