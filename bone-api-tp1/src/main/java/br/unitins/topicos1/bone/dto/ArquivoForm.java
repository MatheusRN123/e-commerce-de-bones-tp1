package br.unitins.topicos1.bone.dto;

import org.jboss.resteasy.reactive.PartType;
import jakarta.ws.rs.FormParam;

public class ArquivoForm {
    
    @FormParam("arquivo")
    @PartType("application/octet-stream")
    public byte[] arquivo;
    
    @FormParam("nomeOriginal")
    public String nomeOriginal;
}
