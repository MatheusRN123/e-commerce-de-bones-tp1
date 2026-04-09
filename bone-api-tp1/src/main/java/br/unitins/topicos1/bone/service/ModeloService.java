package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.ModeloDTO;
import br.unitins.topicos1.bone.dto.ModeloDTOResponse;

public interface ModeloService {
    
    List<ModeloDTOResponse> findAll(int page, int pageSize);
    List<ModeloDTOResponse> findByNome(String nome);
    ModeloDTOResponse findById(Long id);
    ModeloDTOResponse create(ModeloDTO dto);
    void update(Long id, ModeloDTO dto);
    void delete(Long id);
}
