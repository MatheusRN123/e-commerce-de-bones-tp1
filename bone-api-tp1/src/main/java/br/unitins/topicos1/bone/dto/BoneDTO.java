package br.unitins.topicos1.bone.dto;

import java.util.List;

import br.unitins.topicos1.bone.model.Bordado;

public record BoneDTO(
    String nome,
    String cor,
    Long idMaterial,
    String categoriaAba,
    Float tamanhoAba,
    Float profundidade,
    String circunferencia,
    Bordado bordado,
    Long idMarca,
    Long idModelo,
    Integer quantidadeEstoque,
    List<Long> idsEstampas,
    Double preco
) {}