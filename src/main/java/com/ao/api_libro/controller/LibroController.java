package com.ao.api_libro.controller;

import com.ao.api_libro.dto.LibroRequestDTO;
import com.ao.api_libro.dto.LibroResponseDTO;
import com.ao.api_libro.service.LibroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    // ─── GET todos ────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> getAll() {
        return ResponseEntity.ok(libroService.findAll());
    }

    // ─── GET por id ───────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    // ─── POST ─────────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(@Valid @RequestBody LibroRequestDTO dto) {
        LibroResponseDTO creado = libroService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ─── PUT ──────────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LibroRequestDTO dto) {
        return ResponseEntity.ok(libroService.update(id, dto));
    }

    // ─── PATCH ────────────────────────────────────────────────────────────────

    @PatchMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos) {
        return ResponseEntity.ok(libroService.partialUpdate(id, campos));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}