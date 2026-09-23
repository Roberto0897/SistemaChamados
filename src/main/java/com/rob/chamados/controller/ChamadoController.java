package com.rob.chamados.controller;

import com.rob.chamados.dto.ChamadoRequestDTO;
import com.rob.chamados.dto.ChamadoResponseDTO;
import com.rob.chamados.enums.Status;
import com.rob.chamados.service.ChamadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chamados")
@RequiredArgsConstructor
public class ChamadoController {

    private final ChamadoService chamadoService;

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@Valid @RequestBody ChamadoRequestDTO dto,
                                                      Authentication authentication) {
        String email = authentication.getName();
        ChamadoResponseDTO response = chamadoService.criar(dto, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listar(Authentication authentication) {
        String email = authentication.getName();
        List<ChamadoResponseDTO> chamados = chamadoService.listar(email);
        return ResponseEntity.ok(chamados);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> mudarStatus(@PathVariable Long id,
                                                            @RequestParam Status novoStatus) {
        ChamadoResponseDTO response = chamadoService.mudarStatus(id, novoStatus);
        return ResponseEntity.ok(response);
    }
}
