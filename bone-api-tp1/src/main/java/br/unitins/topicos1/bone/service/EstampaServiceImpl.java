package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.EstampaDTOResponse;
import br.unitins.topicos1.bone.model.Estampa;
import br.unitins.topicos1.bone.repository.EstampaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class EstampaServiceImpl implements EstampaService {

    private static final Logger LOG = Logger.getLogger(EstampaServiceImpl.class);

    @Inject
    EstampaRepository repository;

    @Override
    public List<EstampaDTOResponse> findAll(int page, int pageSize) {
        LOG.infof("Buscando estampas [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page não pode ser menor que 0");
            }

            if (pageSize <= 0) {
                throw new IllegalArgumentException("pageSize deve ser maior que 0");
            }

            List<Estampa> estampas = repository
                    .findAll()
                    .page(Page.of(page, pageSize))
                    .list();

            List<EstampaDTOResponse> response = estampas
                .stream()
                .map(EstampaDTOResponse::valueOf)
                .toList();

            LOG.infof("%d estampas encontradas", response.size());
            return response;

        } catch (Exception e) {
            LOG.error("Erro ao buscar todas as estampas", e);
            throw e;
        }
    }

    @Override
    public List<EstampaDTOResponse> findByNome(String nome) {
        LOG.infof("Buscando estampas pelo nome: %s", nome);
        try {
            var estampas = repository.findByNome(nome)
                    .stream()
                    .map(EstampaDTOResponse::valueOf)
                    .toList();
            LOG.infof("Encontradas %d estampas com o nome '%s'", estampas.size(), nome);
            return estampas;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estampas pelo nome: %s", nome);
            throw e;
        }
    }

    @Override
    public EstampaDTOResponse findById(Long id) {
        LOG.infof("Buscando estampa pelo ID: %d", id);
        try {
            Estampa entity = repository.findById(id);
            if (entity == null) {
                LOG.warnf("Estampa ID %d não encontrada", id);
                throw new NotFoundException("Estampa não encontrada");
            }
            LOG.infof("Estampa ID %d encontrada: %s", id, entity.getNome());
            return EstampaDTOResponse.valueOf(entity);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estampa pelo ID: %d", id);
            throw e;
        }
    }

    @Override
    public long count(){
        return repository.count();
    }
}
