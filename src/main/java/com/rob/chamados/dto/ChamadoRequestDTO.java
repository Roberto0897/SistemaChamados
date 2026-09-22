package com.rob.chamados.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChamadoRequestDTO {

    @NotBlank(message = "Título é obrigatório")
    private String titulo;

    private String descricao;

    @NotNull(message = "Prioridade é obrigatória")
    private String prioridade;

    @NotNull(message = "Categoria é obrigatória")
    private Long categoriaId;
}