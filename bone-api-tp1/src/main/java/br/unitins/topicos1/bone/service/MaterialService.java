package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.MaterialDTO;
import br.unitins.topicos1.bone.dto.MaterialDTOResponse;

public interface MaterialService {
    
    List<MaterialDTOResponse> findAll(int page, int pageSize);
    List<MaterialDTOResponse> findByNome(String nome);
    MaterialDTOResponse findById(Long id);
    MaterialDTOResponse create(MaterialDTO dto);
    void update(Long id, MaterialDTO dto);
    void delete(Long id);
    long count();
}
