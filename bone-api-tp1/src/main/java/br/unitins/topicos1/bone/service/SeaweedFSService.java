package br.unitins.topicos1.bone.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SeaweedFSService {

    @ConfigProperty(name = "seaweedfs.master.url")
    String masterUrl;

    @ConfigProperty(name = "seaweedfs.request-timeout-ms", defaultValue = "10000")
    Long requestTimeoutMs;

    @Inject
    HashService hashService;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    public ArquivoUploadResponse uploadFile(byte[] fileContent, String fileName) throws IOException {
        try {
            String fid = obterFid();
            String url = masterUrl + "/" + fid;
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofByteArray(fileContent))
                .header("Content-Type", "application/octet-stream")
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                String sha256 = hashService.gerarSHA256(fileContent);
                
                return new ArquivoUploadResponse(
                    fid,
                    fileName,
                    "application/octet-stream",
                    (long) fileContent.length,
                    sha256
                );
            } else {
                throw new IOException("Erro ao fazer upload: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.error("Erro ao fazer upload no SeaweedFS", e);
            throw new IOException("Erro ao fazer upload no SeaweedFS", e);
        } catch (Exception e) {
            Log.error("Erro ao fazer upload no SeaweedFS", e);
            throw new IOException("Erro ao fazer upload no SeaweedFS", e);
        }
    }

    private String obterFid() throws IOException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(masterUrl + "/dir/assign"))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String fid = root.get("fid").asText();
                return fid;
            } else {
                throw new IOException("Erro ao obter FID: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.error("Erro ao obter FID do SeaweedFS", e);
            throw new IOException("Erro ao obter FID", e);
        } catch (Exception e) {
            Log.error("Erro ao obter FID do SeaweedFS", e);
            throw new IOException("Erro ao obter FID", e);
        }
    }

    public byte[] downloadFile(String fid) throws IOException {
        try {
            String url = masterUrl + "/" + fid;
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
            
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("Erro ao fazer download: " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Log.error("Erro ao fazer download no SeaweedFS", e);
            throw new IOException("Erro ao fazer download", e);
        } catch (Exception e) {
            Log.error("Erro ao fazer download no SeaweedFS", e);
            throw new IOException("Erro ao fazer download", e);
        }
    }

    public static class ArquivoUploadResponse {
        public String fid;
        public String nomeOriginal;
        public String mimeType;
        public Long tamanhoBytes;
        public String sha256;

        public ArquivoUploadResponse(String fid, String nomeOriginal, String mimeType, 
                                    Long tamanhoBytes, String sha256) {
            this.fid = fid;
            this.nomeOriginal = nomeOriginal;
            this.mimeType = mimeType;
            this.tamanhoBytes = tamanhoBytes;
            this.sha256 = sha256;
        }
    }
}
