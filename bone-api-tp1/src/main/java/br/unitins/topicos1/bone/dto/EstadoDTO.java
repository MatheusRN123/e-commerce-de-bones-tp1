package br.unitins.topicos1.bone.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record EstadoDTO(
    @NotNull(message = "O nome do estado é obrigatório")
    String nome,
    @NotNull(message = "A sigla do estado é obrigatória")
    String sigla,
    List<Long> idsCidades
) {}
