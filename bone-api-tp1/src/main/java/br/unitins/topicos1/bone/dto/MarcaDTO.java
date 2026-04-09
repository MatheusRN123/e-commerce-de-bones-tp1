package br.unitins.topicos1.bone.dto;

import jakarta.validation.constraints.NotBlank;

public record MarcaDTO(
    @NotBlank(message = "O campo nome deve ser informado (back).")
    String nome
) {}
