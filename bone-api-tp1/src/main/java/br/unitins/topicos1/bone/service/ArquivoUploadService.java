package br.unitins.topicos1.bone.service;

import java.io.IOException;

import br.unitins.topicos1.bone.dto.ArquivoResponseDTO;
import br.unitins.topicos1.bone.model.Arquivo;
import br.unitins.topicos1.bone.repository.ArquivoRepository;
import br.unitins.topicos1.bone.service.SeaweedFSService.ArquivoUploadResponse;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ArquivoUploadService {

    @Inject
    SeaweedFSService seaweedFSService;

    @Inject
    ArquivoRepository arquivoRepository;

    @Transactional
    public ArquivoResponseDTO salvarArquivo(byte[] conteudo, String nomeOriginal) throws IOException {
        try {
            ArquivoUploadResponse uploadResponse = seaweedFSService.uploadFile(conteudo, nomeOriginal);

            Arquivo arquivo = new Arquivo();
            arquivo.setFid(uploadResponse.fid);
            arquivo.setNomeOriginal(uploadResponse.nomeOriginal);
            arquivo.setMimeType(uploadResponse.mimeType);
            arquivo.setTamanhoBytes(uploadResponse.tamanhoBytes);
            arquivo.setSha256(uploadResponse.sha256);

            arquivoRepository.persist(arquivo);

            return ArquivoResponseDTO.valueOf(arquivo);
        } catch (IOException e) {
            Log.error("Erro ao salvar arquivo", e);
            throw e;
        }
    }

    public byte[] obterArquivo(String fid) throws IOException {
        return seaweedFSService.downloadFile(fid);
    }
}
