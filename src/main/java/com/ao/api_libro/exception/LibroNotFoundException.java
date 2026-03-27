package com.ao.api_libro.exception;

public class LibroNotFoundException extends RuntimeException {

    public LibroNotFoundException(Long id) {
        super("Libro con id " + id + " no encontrado");
    }
}