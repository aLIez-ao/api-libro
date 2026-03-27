package com.ao.api_libro.service;

import com.ao.api_libro.dto.LibroRequestDTO;
import com.ao.api_libro.dto.LibroResponseDTO;
import com.ao.api_libro.exception.LibroNotFoundException;
import com.ao.api_libro.model.Libro;
import com.ao.api_libro.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;

    // ─── Mappers ──────────────────────────────────────────────────────────────

    private Libro toEntity(LibroRequestDTO dto) {
        return Libro.builder()
                .titulo(dto.getTitulo())
                .autor(dto.getAutor())
                .editorial(dto.getEditorial())
                .correoEditorial(dto.getCorreoEditorial())
                .precio(dto.getPrecio())
                .genero(dto.getGenero())
                .build();
    }

    private LibroResponseDTO toDTO(Libro libro) {
        return LibroResponseDTO.builder()
                .id(libro.getId())
                .titulo(libro.getTitulo())
                .autor(libro.getAutor())
                .editorial(libro.getEditorial())
                .correoEditorial(libro.getCorreoEditorial())
                .precio(libro.getPrecio())
                .genero(libro.getGenero())
                .build();
    }

    // ─── SELECT todos ─────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponseDTO> findAll() {
        return libroRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── SELECT por id ────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public LibroResponseDTO findById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException(id));
        return toDTO(libro);
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public LibroResponseDTO save(LibroRequestDTO dto) {
        Libro libro = toEntity(dto);
        Libro guardado = libroRepository.save(libro);
        return toDTO(guardado);
    }

    // ─── UPDATE completo ──────────────────────────────────────────────────────

    @Override
    @Transactional
    public LibroResponseDTO update(Long id, LibroRequestDTO dto) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException(id));

        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setEditorial(dto.getEditorial());
        libro.setCorreoEditorial(dto.getCorreoEditorial());
        libro.setPrecio(dto.getPrecio());
        libro.setGenero(dto.getGenero());

        Libro actualizado = libroRepository.save(libro);
        return toDTO(actualizado);
    }

    // ─── UPDATE parcial ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public LibroResponseDTO partialUpdate(Long id, Map<String, Object> campos) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException(id));

        campos.forEach((campo, valor) -> {
            switch (campo) {
                case "titulo"          -> libro.setTitulo((String) valor);
                case "autor"           -> libro.setAutor((String) valor);
                case "editorial"       -> libro.setEditorial((String) valor);
                case "correoEditorial" -> libro.setCorreoEditorial((String) valor);
                case "precio"          -> libro.setPrecio(new BigDecimal(valor.toString()));
                case "genero"          -> libro.setGenero((String) valor);
            }
        });

        Libro actualizado = libroRepository.save(libro);
        return toDTO(actualizado);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNotFoundException(id);
        }
        libroRepository.deleteById(id);
    }
}