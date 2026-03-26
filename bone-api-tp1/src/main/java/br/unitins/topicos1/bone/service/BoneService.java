package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.BoneDTO;
import br.unitins.topicos1.bone.dto.BoneDTOResponse;

public interface BoneService {
    List<BoneDTOResponse> findAll(int page, int pageSize);
    List<BoneDTOResponse> findByNome(String nome);
    BoneDTOResponse findById(Long id);
    BoneDTOResponse create(BoneDTO dto);
    void update(Long id, BoneDTO dto);
    void delete(Long id);
    long count();
}