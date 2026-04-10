package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.MarcaDTO;
import br.unitins.topicos1.bone.dto.MarcaDTOResponse;
import br.unitins.topicos1.bone.model.Marca;
import br.unitins.topicos1.bone.repository.MarcaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    private static final Logger LOG = Logger.getLogger(MarcaServiceImpl.class);
    
    @Inject
    MarcaRepository repository;

    @Override
    public List<MarcaDTOResponse> findAll(int page, int pageSize) {
        LOG.infof("Buscando marcas [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page não pode ser menor que 0");
            }

            if (pageSize <= 0) {
                throw new IllegalArgumentException("pageSize deve ser maior que 0");
            }

            List<Marca> marcas = repository
                    .findAll()
                    .page(Page.of(page, pageSize))
                    .list();

            List<MarcaDTOResponse> response = marcas
                .stream()
                .map(MarcaDTOResponse::valueOf)
                .toList();

            LOG.infof("%d marcas encontradas", response.size());
            return response;

        } catch (Exception e) {
            LOG.error("Erro ao buscar todas as marcas", e);
            throw e;
        }
    }

    @Override
    public List<MarcaDTOResponse> findByNome(String nome){
        LOG.infof("Requisição para buscar marcas pelo nome: %s", nome);
        var marcas = repository.findByNome(nome)
                .stream()
                .map(MarcaDTOResponse::valueOf)
                .toList();
        LOG.infof("Encontradas %d marcas com o nome '%s'", marcas.size(), nome);
        return marcas;
    }

    @Override
    public MarcaDTOResponse findById(Long id){
        LOG.infof("Requisição para buscar marca pelo ID: %d", id);
        MarcaDTOResponse response = MarcaDTOResponse.valueOf(repository.findById(id));
        LOG.infof("Marca encontrada: %s", response.nome());
        return response;
    }

    @Override
    @Transactional
    public MarcaDTOResponse create(MarcaDTO dto){
        LOG.infof("Requisição para criar marca: %s", dto.nome());

        Marca marca = new Marca();
        marca.setNome(dto.nome());

        repository.persist(marca);

        LOG.infof("Marca '%s' criada com sucesso", dto.nome());
        return MarcaDTOResponse.valueOf(marca);
    }

    @Override
    @Transactional
    public void update(Long id, MarcaDTO dto){
        LOG.infof("Requisição para atualizar marca ID: %d", id);
        Marca marca = repository.findById(id);
        marca.setNome(dto.nome());
        LOG.infof("Marca ID %d atualizada com sucesso", id);
    }

    @Override
    @Transactional
    public void delete(Long id){
        LOG.infof("Requisição para deletar marca ID: %d", id);
        repository.deleteById(id);
        LOG.infof("Marca ID %d deletada com sucesso", id);
    }

    @Override
    public long count(){
        return repository.count();
    }
}
