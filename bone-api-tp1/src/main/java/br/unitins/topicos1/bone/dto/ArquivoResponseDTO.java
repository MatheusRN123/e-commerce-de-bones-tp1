package br.unitins.topicos1.bone.dto;

import br.unitins.topicos1.bone.model.Arquivo;

public record ArquivoResponseDTO(
    Long id,
    String fid,
    String nomeOriginal,
    String mimeType,
    Long tamanhoBytes,
    String sha256
) {
    public static ArquivoResponseDTO valueOf(Arquivo arquivo) {
        return new ArquivoResponseDTO(
            arquivo.getId(),
            arquivo.getFid(),
            arquivo.getNomeOriginal(),
            arquivo.getMimeType(),
            arquivo.getTamanhoBytes(),
            arquivo.getSha256()
        );
    }
}