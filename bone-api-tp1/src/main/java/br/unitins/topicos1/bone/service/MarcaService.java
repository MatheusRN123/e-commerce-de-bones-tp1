package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.MarcaDTO;
import br.unitins.topicos1.bone.dto.MarcaDTOResponse;

public interface MarcaService {
    
    List<MarcaDTOResponse> findAll(int page, int pageSize);
    List<MarcaDTOResponse> findByNome(String nome);
    MarcaDTOResponse findById(Long id);
    MarcaDTOResponse create(MarcaDTO dto);
    void update(Long id, MarcaDTO dto);
    void delete(Long id);
}