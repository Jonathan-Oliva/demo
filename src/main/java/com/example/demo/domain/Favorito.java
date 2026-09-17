package com.example.demo.domain;

import java.time.LocalDate;

public class Favorito {
    private Long id;
    private Long idProductoExterno;
    private String notaPersonal;
    private LocalDate fechaAgregado;

    public Favorito() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdProductoExterno() {
        return idProductoExterno;
    }

    public void setIdProductoExterno(Long idProductoExterno) {
        this.idProductoExterno = idProductoExterno;
    }

    public String getNotaPersonal() {
        return notaPersonal;
    }

    public void setNotaPersonal(String notaPersonal) {
        this.notaPersonal = notaPersonal;
    }

    public LocalDate getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDate fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }
}
