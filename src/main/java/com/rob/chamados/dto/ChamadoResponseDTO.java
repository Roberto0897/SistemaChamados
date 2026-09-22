package com.rob.chamados.dto;

import com.rob.chamados.enums.Prioridade;
import com.rob.chamados.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ChamadoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private Status status;
    private Prioridade prioridade;
    private LocalDateTime dataCriacao;
    private String categoriaNome;
    private String solicitanteNome;
}
