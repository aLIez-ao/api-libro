package com.ao.api_libro.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "libro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    @Column(name = "autor", nullable = false, length = 255)
    private String autor;

    @Column(name = "editorial", nullable = false, length = 255)
    private String editorial;

    @Column(name = "correo_editorial", nullable = false, length = 255)
    private String correoEditorial;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "genero", nullable = false, length = 100)
    private String genero;
}