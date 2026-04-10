package br.unitins.topicos1.bone.service;

import br.unitins.topicos1.bone.dto.EstoqueDTO;
import br.unitins.topicos1.bone.dto.EstoqueDTOResponse;
import br.unitins.topicos1.bone.model.Bone;
import br.unitins.topicos1.bone.model.Estoque;
import br.unitins.topicos1.bone.repository.BoneRepository;
import br.unitins.topicos1.bone.repository.EstoqueRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import org.jboss.logging.Logger;
import io.quarkus.panache.common.Page;

@ApplicationScoped
public class EstoqueServiceImpl implements EstoqueService {

    private static final Logger LOG = Logger.getLogger(EstoqueServiceImpl.class);

    @Inject
    EstoqueRepository repository;

    @Inject
    BoneRepository boneRepository;

    @Override
    public List<EstoqueDTOResponse> findAll(int page, int pageSize) {
        LOG.infof("Buscando estoques [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page não pode ser menor que 0");
            }

            if (pageSize <= 0) {
                throw new IllegalArgumentException("pageSize deve ser maior que 0");
            }

            List<Estoque> estoques = repository
                    .findAll()
                    .page(Page.of(page, pageSize))
                    .list();

            List<EstoqueDTOResponse> response = estoques
                .stream()
                .map(EstoqueDTOResponse::valueOf)
                .toList();

            LOG.infof("%d estoques encontrados", response.size());
            return response;

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os estoques", e);
            throw e;
        }
    }

    @Override
    public EstoqueDTOResponse findById(Long id){
        LOG.infof("Buscando estoque por ID: %d", id);

        try{
            Estoque estoque = repository.findById(id);

            if(estoque == null){
                LOG.warnf("Estoque ID %d não encontrado", id);
                return null;
            }

            return EstoqueDTOResponse.valueOf(estoque);
            
        }catch(Exception e){
            LOG.errorf(e, "Erro ao buscar estoque ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public Boolean verificarDisponibilidade(Long id) {
        LOG.infof("Verificando disponibilidade do estoque ID: %d", id);
        try {
            Estoque estoque = repository.findById(id);
            boolean disponivel = estoque.verificarDisponibilidade();
            LOG.infof("Estoque ID %d disponível: %b", id, disponivel);
            return disponivel;
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao verificar disponibilidade do estoque ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public void atualizarQuantidade(Long id, EstoqueDTO dto) {
        LOG.infof("Atualizando quantidade do estoque ID: %d", id);
        try {
            Estoque estoque = repository.findById(id);
            if (estoque != null) {
                estoque.atualizarQuantidade(dto.quantidade());
                LOG.infof("Quantidade do estoque ID %d atualizada", id);
            } else {
                LOG.warnf("Estoque ID %d não encontrado para atualizar quantidade", id);
            }
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar quantidade do estoque ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public void adicionarQuantidade(Long id, EstoqueDTO dto) {
        LOG.infof("Adicionando quantidade ao estoque ID: %d", id);
        try {
            Estoque estoque = repository.findById(id);
            if (estoque != null) {
                estoque.adicionarQuantidade(dto.quantidade());
                LOG.infof("Quantidade adicionada ao estoque ID %d com sucesso", id);
            } else {
                LOG.warnf("Estoque ID %d não encontrado para adicionar quantidade", id);
            }
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao adicionar quantidade ao estoque ID: %d", id);
            throw e;
        }
    }

    @Override
    public EstoqueDTOResponse findByIdBone(Long boneId){
        LOG.infof("Buscando estoque pelo ID do boné: %d", boneId);
    
        try{
            Bone bone = boneRepository.findById(boneId);
        
            if (bone == null) {
                LOG.warnf("Boné ID %d não encontrado", boneId);
                return null;
            }
        
            Estoque estoque = bone.getEstoque();
        
            if(estoque == null){
                LOG.warnf("Estoque para boné ID %d não encontrado", boneId);
                return null;
            }
        
            return EstoqueDTOResponse.valueOf(estoque);
        
        }catch(Exception e){
            LOG.errorf(e, "Erro ao buscar estoque para boné ID: %d", boneId);
            throw e;
        }
    }

    @Override    public long count(){
        return repository.count();
    }
}
