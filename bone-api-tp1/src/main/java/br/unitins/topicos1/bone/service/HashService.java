package br.unitins.topicos1.bone.service;

public interface HashService {
    
    String getHashSenha(String senha);
    
    String gerarSHA256(byte[] dados);
}
