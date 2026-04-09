package br.unitins.topicos1.bone.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record EstoqueDTO(
    @NotNull(message = "O campo quantidade deve ser informado (back).")
    @Min(value = 0, message = "A quantidade deve ser maior ou igual a 0 (back).")
    Integer quantidade,
    
    @NotNull(message = "O campo dataAtualizacao deve ser informado (back).")
    LocalDate dataAtualizacao
) {}
