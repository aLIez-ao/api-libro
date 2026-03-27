package com.ao.api_libro.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroResponseDTO {

    private Long id;
    private String titulo;
    private String autor;
    private String editorial;
    private String correoEditorial;
    private BigDecimal precio;
    private String genero;
}