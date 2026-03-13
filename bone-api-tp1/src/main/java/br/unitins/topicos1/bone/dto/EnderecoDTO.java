package br.unitins.topicos1.bone.dto;


public record EnderecoDTO(
    String nomeDestinatario,
    String cep,
    String logradouro,
    String numero,
    Long idCidade
) {}
