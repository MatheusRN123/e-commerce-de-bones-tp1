package br.unitins.topicos1.bone.service;

import br.unitins.topicos1.bone.dto.EstampaDigitalDTO;
import br.unitins.topicos1.bone.dto.EstampaDigitalDTOResponse;
import br.unitins.topicos1.bone.model.EstampaDigital;
import br.unitins.topicos1.bone.repository.EstampaDigitalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EstampaDigitalServiceImpl implements EstampaDigitalService {

    private static final Logger LOG = Logger.getLogger(EstampaDigitalServiceImpl.class);

    @Inject
    EstampaDigitalRepository repository;

    @Override
    @Transactional
    public EstampaDigitalDTOResponse create(EstampaDigitalDTO dto) {
        LOG.infof("Criando estampa digital: %s", dto.nome());
        try {
            EstampaDigital estampa = new EstampaDigital();
            estampa.setNome(dto.nome());
            estampa.setPosicao(dto.posicao());
            estampa.setDescricao(dto.descricao());
            estampa.setResolucao(dto.resolucao());

            repository.persist(estampa);
            LOG.infof("Estampa digital '%s' criada com sucesso", dto.nome());
            return EstampaDigitalDTOResponse.valueOf(estampa);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar estampa digital: %s", dto.nome());
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Long id, EstampaDigitalDTO dto) {
        LOG.infof("Atualizando estampa digital ID: %d", id);
        try {
            EstampaDigital estampa = repository.findById(id);
            if (estampa == null) {
                LOG.warnf("Estampa digital ID %d não encontrada para atualização", id);
                throw new NotFoundException("Estampa digital não encontrada");
            }

            estampa.setNome(dto.nome());
            estampa.setPosicao(dto.posicao());
            estampa.setDescricao(dto.descricao());
            estampa.setResolucao(dto.resolucao());

            LOG.infof("Estampa digital ID %d atualizada com sucesso", id);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar estampa digital ID: %d", id);
            throw e;
        }
    }
}
