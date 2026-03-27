package com.ao.api_libro.service;

import com.ao.api_libro.dto.LibroRequestDTO;
import com.ao.api_libro.dto.LibroResponseDTO;

import java.util.List;
import java.util.Map;

public interface LibroService {
    List<LibroResponseDTO> findAll();

    LibroResponseDTO findById(Long id);

    LibroResponseDTO save(LibroRequestDTO dto);

    List<LibroResponseDTO> saveAll(List<LibroRequestDTO> dtos);

    LibroResponseDTO update(Long id, LibroRequestDTO dto);

    LibroResponseDTO partialUpdate(Long id, Map<String, Object> campos);

    void deleteById(Long id);
}