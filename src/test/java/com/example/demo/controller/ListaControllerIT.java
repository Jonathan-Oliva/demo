package com.example.demo.controller;

import com.example.demo.dto.ListaDTO;
import com.example.demo.dto.NuevaListaDTO;
import com.example.demo.dto.RegistroUsuarioDTO;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.repository.UsuarioJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class ListaControllerIT {
    @Container
    private static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17")
            .withDatabaseName("web2_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configurarPostgres(DynamicPropertyRegistry properties) {
        properties.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        properties.add("spring.datasource.username", POSTGRES::getUsername);
        properties.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired
    private TestRestTemplate http;
    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    @Test
    void login_credencialesValidas_devuelveToken() {
        String email = registrarUsuario();

        ResponseEntity<Map<String, Object>> response = http.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email, "password", "clave-segura")),
                new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().get("token") instanceof String);
        assertFalse(((String) response.getBody().get("token")).isBlank());
    }

    @Test
    void login_credencialesInvalidas_responde401() {
        String email = registrarUsuario();

        ResponseEntity<String> response = http.postForEntity(
                "/api/auth/login",
                Map.of("email", email, "password", "incorrecta"),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getLista_listaPropia_responde200() {
        String token = iniciarSesion(registrarUsuario());
        ListaDTO lista = crearLista(token);

        ResponseEntity<ListaDTO> response = http.exchange(
                "/api/listas/{id}", HttpMethod.GET, conToken(token), ListaDTO.class, lista.id());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista.id(), response.getBody().id());
    }

    @Test
    void getLista_listaAjena_responde403() {
        ListaDTO listaAjena = crearLista(iniciarSesion(registrarUsuario()));
        String token = iniciarSesion(registrarUsuario());

        ResponseEntity<String> response = http.exchange(
                "/api/listas/{id}", HttpMethod.GET, conToken(token), String.class, listaAjena.id());

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void listarListas_sinToken_responde401() {
        ResponseEntity<String> response = http.getForEntity("/api/listas", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getLista_adminSobreListaAjena_responde200() {
        ListaDTO listaAjena = crearLista(iniciarSesion(registrarUsuario()));
        String adminEmail = registrarUsuario();
        UsuarioEntity admin = usuarioJpaRepository.findByEmail(adminEmail).orElseThrow();
        admin.setRol("ADMIN");
        usuarioJpaRepository.save(admin);
        String adminToken = iniciarSesion(adminEmail);

        ResponseEntity<ListaDTO> response = http.exchange(
                "/api/listas/{id}", HttpMethod.GET, conToken(adminToken), ListaDTO.class, listaAjena.id());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listaAjena.id(), response.getBody().id());
    }

    private String registrarUsuario() {
        String email = "user-" + UUID.randomUUID() + "@example.com";
        ResponseEntity<String> response = http.postForEntity(
                "/api/auth/registro",
                new RegistroUsuarioDTO(email, "clave-segura"),
                String.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return email;
    }

    private String iniciarSesion(String email) {
        ResponseEntity<Map<String, Object>> response = http.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(Map.of("email", email, "password", "clave-segura")),
                new ParameterizedTypeReference<>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        return (String) response.getBody().get("token");
    }

    private ListaDTO crearLista(String token) {
        ResponseEntity<ListaDTO> response = http.exchange(
                "/api/listas",
                HttpMethod.POST,
                new HttpEntity<>(new NuevaListaDTO("Lista de prueba"), headersConToken(token)),
                ListaDTO.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        return response.getBody();
    }

    private HttpEntity<Void> conToken(String token) {
        return new HttpEntity<>(headersConToken(token));
    }

    private HttpHeaders headersConToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
