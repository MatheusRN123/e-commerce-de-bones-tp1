package br.unitins.topicos1.bone.dto;

import jakarta.validation.constraints.NotBlank;

public record ModeloDTO(
    @NotBlank(message = "O campo nome deve ser informado (back).")
    String nome,
    
    String categoria,
    
    String estilo
) {}
