package com.example.demo.dto;

public record TokenDTO(String token, String tipo) {
    public TokenDTO(String token) {
        this(token, "Bearer");
    }
}
