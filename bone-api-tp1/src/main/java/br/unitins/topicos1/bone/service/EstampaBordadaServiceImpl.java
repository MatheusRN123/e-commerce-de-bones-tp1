package br.unitins.topicos1.bone.service;

import br.unitins.topicos1.bone.dto.EstampaBordadaDTO;
import br.unitins.topicos1.bone.dto.EstampaBordadaDTOResponse;
import br.unitins.topicos1.bone.model.EstampaBordada;
import br.unitins.topicos1.bone.repository.EstampaBordadaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EstampaBordadaServiceImpl implements EstampaBordadaService {

    private static final Logger LOG = Logger.getLogger(EstampaBordadaServiceImpl.class);

    @Inject
    EstampaBordadaRepository repository;

    @Override
    @Transactional
    public EstampaBordadaDTOResponse create(EstampaBordadaDTO dto) {
        LOG.infof("Criando estampa bordada: %s", dto.nome());
        try {
            EstampaBordada estampa = new EstampaBordada();
            estampa.setNome(dto.nome());
            estampa.setPosicao(dto.posicao());
            estampa.setDescricao(dto.descricao());
            estampa.setCorLinha(dto.corLinha());
            estampa.setQuantCores(dto.quantCores());

            repository.persist(estampa);
            LOG.infof("Estampa bordada '%s' criada com sucesso", dto.nome());
            return EstampaBordadaDTOResponse.valueOf(estampa);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar estampa bordada: %s", dto.nome());
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Long id, EstampaBordadaDTO dto) {
        LOG.infof("Atualizando estampa bordada ID: %d", id);
        try {
            EstampaBordada estampa = repository.findById(id);
            if (estampa == null) {
                LOG.warnf("Estampa bordada ID %d não encontrada para atualização", id);
                throw new NotFoundException("Estampa bordada não encontrada");
            }

            estampa.setNome(dto.nome());
            estampa.setPosicao(dto.posicao());
            estampa.setDescricao(dto.descricao());
            estampa.setCorLinha(dto.corLinha());
            estampa.setQuantCores(dto.quantCores());

            LOG.infof("Estampa bordada ID %d atualizada com sucesso", id);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar estampa bordada ID: %d", id);
            throw e;
        }
    }
}
