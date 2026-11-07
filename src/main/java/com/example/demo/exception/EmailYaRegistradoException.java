package com.example.demo.exception;

public class EmailYaRegistradoException extends RuntimeException {
    public EmailYaRegistradoException(String email) {
        super("El email ya está registrado: " + email);
    }
}
