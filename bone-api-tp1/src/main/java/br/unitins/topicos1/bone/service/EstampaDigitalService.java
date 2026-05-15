package br.unitins.topicos1.bone.service;


import br.unitins.topicos1.bone.dto.EstampaDigitalDTO;
import br.unitins.topicos1.bone.dto.EstampaDigitalDTOResponse;

public interface EstampaDigitalService {
    
    EstampaDigitalDTOResponse create(EstampaDigitalDTO dto);
    void update(Long id, EstampaDigitalDTO dto);
}
