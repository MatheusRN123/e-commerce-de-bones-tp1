package br.unitins.topicos1.bone.service;

public record ArquivoDownload(
    byte[] content,
    String contentType,
    String fileName
) {
}