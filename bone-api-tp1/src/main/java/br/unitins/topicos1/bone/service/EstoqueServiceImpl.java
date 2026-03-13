package br.unitins.topicos1.bone.service;

import br.unitins.topicos1.bone.dto.EstoqueDTO;
import br.unitins.topicos1.bone.dto.EstoqueDTOResponse;
import br.unitins.topicos1.bone.model.Estoque;
import br.unitins.topicos1.bone.repository.EstoqueRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

import org.jboss.logging.Logger;

@ApplicationScoped
public class EstoqueServiceImpl implements EstoqueService {

    private static final Logger LOG = Logger.getLogger(EstoqueServiceImpl.class);

    @Inject
    EstoqueRepository repository;

    @Override
    public List<EstoqueDTOResponse> findAll(){
        LOG.info("Buscando todos os estoques");

        try{
            return repository
                    .listAll()
                    .stream()
                    .map(EstoqueDTOResponse::valueOf)
                    .toList();

        } catch(Exception e){
            LOG.error("Erro ao buscar estoques", e);
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
}
