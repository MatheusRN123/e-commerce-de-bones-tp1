package br.unitins.topicos1.bone.resource;

import java.io.IOException;

import br.unitins.topicos1.bone.dto.ArquivoResponseDTO;
import br.unitins.topicos1.bone.service.ArquivoUploadService;
import io.quarkus.logging.Log;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/arquivos")
@ApplicationScoped
public class ArquivoResource {

    @Inject
    ArquivoUploadService uploadService;

    @POST
    @Path("/upload")
    @RolesAllowed("ADM")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@RestForm("arquivo") FileUpload arquivo) {
        try {
            Log.info("Iniciando upload do arquivo: " + arquivo.fileName());
            
            byte[] conteudo = java.nio.file.Files.readAllBytes(arquivo.filePath());
            ArquivoResponseDTO resposta = uploadService.salvarArquivo(conteudo, arquivo.fileName());
            
            Log.info("Upload realizado com sucesso - FID: " + resposta.fid());
            return Response.ok(resposta).build();
        } catch (IOException e) {
            Log.error("Erro ao fazer upload do arquivo", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Erro ao fazer upload: " + e.getMessage())
                .build();
        }
    }

    @GET
    @Path("/download/{fid}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("fid") String fid) {
        try {
            Log.info("Iniciando download do arquivo - FID: " + fid);
            
            byte[] conteudo = uploadService.obterArquivo(fid);
            
            return Response.ok(conteudo)
                .header("Content-Disposition", "attachment; filename=\"arquivo\"")
                .build();
        } catch (IOException e) {
            Log.error("Erro ao fazer download do arquivo", e);
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Arquivo não encontrado")
                .build();
        }
    }
}
