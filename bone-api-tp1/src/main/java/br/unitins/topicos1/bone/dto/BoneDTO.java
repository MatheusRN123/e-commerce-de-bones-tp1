package br.unitins.topicos1.bone.dto;

import java.util.List;

import br.unitins.topicos1.bone.model.Bordado;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoneDTO(
    @NotBlank(message = "O campo deve ser informado (back).")
    String nome,

    @NotBlank(message = "O campo deve ser informado (back).")
    String cor,

    @NotNull(message = "O campo deve ser informado (back).")
    Long idMaterial,

    String categoriaAba,

    Float tamanhoAba,

    Float profundidade,

    String circunferencia,

    Bordado bordado,

    @NotNull(message = "O campo deve ser informado (back).")
    Long idMarca,

    @NotNull(message = "O campo deve ser informado (back).")
    Long idModelo,

    @Min(1)
    @NotNull(message = "A quantidade em estoque deve ser informada e ser maior que zero (back).")
    Integer quantidadeEstoque,

    List<Long> idsEstampas,

    @NotNull(message = "O campo deve ser informado (back).")
    Double preco
) {}