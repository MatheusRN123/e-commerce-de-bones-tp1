package br.unitins.topicos1.bone.dto;

import jakarta.validation.constraints.NotBlank;

public record EstampaBordadaDTO(
    @NotBlank(message = "O campo nome deve ser informado (back).")
    String nome,
    
    @NotBlank(message = "O campo tipo deve ser informado (back).")
    String tipo,
    
    String posicao,
    
    String descricao,
    
    String corLinha,
    
    Integer quantCores
) {}
