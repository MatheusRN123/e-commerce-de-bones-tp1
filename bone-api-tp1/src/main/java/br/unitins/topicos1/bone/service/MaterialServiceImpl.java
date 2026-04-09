package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.MaterialDTO;
import br.unitins.topicos1.bone.dto.MaterialDTOResponse;
import br.unitins.topicos1.bone.model.Material;
import br.unitins.topicos1.bone.repository.MaterialRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class MaterialServiceImpl implements MaterialService {
    
    private static final Logger LOG = Logger.getLogger(MaterialServiceImpl.class);

    @Inject
    MaterialRepository repository;

    @Override
    public List<MaterialDTOResponse> findAll(int page, int pageSize) {
        LOG.infof("Buscando materiais [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page não pode ser menor que 0");
            }

            if (pageSize <= 0) {
                throw new IllegalArgumentException("pageSize deve ser maior que 0");
            }

            List<Material> materiais = repository
                    .findAll()
                    .page(Page.of(page, pageSize))
                    .list();

            List<MaterialDTOResponse> response = materiais
                .stream()
                .map(MaterialDTOResponse::valueOf)
                .toList();

            LOG.infof("%d materiais encontrados", response.size());
            return response;

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os materiais", e);
            throw e;
        }
    }

    @Override
    public List<MaterialDTOResponse> findByNome(String nome){
        LOG.infof("Buscando materiais pelo nome: %s", nome);
        List<MaterialDTOResponse> materiais = repository
                    .findByNome(nome)
                    .stream()
                    .map(MaterialDTOResponse::valueOf)
                    .toList();
        LOG.infof("Encontrados %d materiais com o nome '%s'", materiais.size(), nome);
        return materiais;
    }

    @Override
    public MaterialDTOResponse findById(Long id){
        LOG.infof("Buscando material pelo ID: %d", id);
        MaterialDTOResponse material = MaterialDTOResponse.valueOf(repository.findById(id));
        LOG.infof("Material encontrado: %s", material.nome());
        return material;
    }

    @Override
    @Transactional
    public MaterialDTOResponse create(MaterialDTO dto){
        LOG.infof("Criando material: %s", dto.nome());
        Material material = new Material();
        material.setNome(dto.nome());
        repository.persist(material);
        LOG.infof("Material '%s' criado com sucesso", dto.nome());
        return MaterialDTOResponse.valueOf(material);
    }

    @Override
    @Transactional
    public void update(Long id, MaterialDTO dto){
        LOG.infof("Atualizando material ID: %d", id);
        Material material = repository.findById(id);
        material.setNome(dto.nome());
        LOG.infof("Material ID %d atualizado com sucesso", id);
    }

    @Override
    @Transactional
    public void delete(Long id){
        LOG.infof("Deletando material ID: %d", id);
        repository.deleteById(id);
        LOG.infof("Material ID %d deletado com sucesso", id);
    }
}
