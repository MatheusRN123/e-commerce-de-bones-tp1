package br.unitins.topicos1.bone.service;

import br.unitins.topicos1.bone.dto.EstampaBordadaDTO;
import br.unitins.topicos1.bone.dto.EstampaBordadaDTOResponse;

public interface EstampaBordadaService {
    EstampaBordadaDTOResponse create(EstampaBordadaDTO dto);
    void update(Long id, EstampaBordadaDTO dto);
}
