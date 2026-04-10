package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.EstampaDTOResponse;

public interface EstampaService {
    
    List<EstampaDTOResponse> findAll(int page, int pageSize);
    List<EstampaDTOResponse> findByNome(String nome);
    EstampaDTOResponse findById(Long id);
    long count();
}
