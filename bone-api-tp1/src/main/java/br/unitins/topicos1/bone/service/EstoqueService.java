package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.EstoqueDTO;
import br.unitins.topicos1.bone.dto.EstoqueDTOResponse;

public interface EstoqueService {
    
    Boolean verificarDisponibilidade(Long id);
    void atualizarQuantidade(Long id, EstoqueDTO dto);
    void adicionarQuantidade(Long id, EstoqueDTO dto);
    List<EstoqueDTOResponse> findAll(int page, int pageSize);
    EstoqueDTOResponse findById(Long id);
}