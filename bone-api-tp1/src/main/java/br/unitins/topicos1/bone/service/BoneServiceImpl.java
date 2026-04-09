package br.unitins.topicos1.bone.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.unitins.topicos1.bone.dto.BoneDTO;
import br.unitins.topicos1.bone.dto.BoneDTOResponse;
import br.unitins.topicos1.bone.model.*;
import br.unitins.topicos1.bone.repository.*;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import org.jboss.logging.Logger;

@ApplicationScoped
public class BoneServiceImpl implements BoneService {

    private static final Logger LOG = Logger.getLogger(BoneServiceImpl.class);

    @Inject
    BoneRepository repository;

    @Inject
    MaterialRepository repositoryMaterial;

    @Inject
    MarcaRepository repositoryMarca;

    @Inject
    ModeloRepository repositoryModelo;

    @Inject
    EstoqueRepository repositoryEstoque;

    @Inject
    EstampaRepository repositoryEstampa;


    @Override
    public List<BoneDTOResponse> findAll(int page, int pageSize) {
        LOG.infof("Buscando bonés [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                throw new IllegalArgumentException("Page não pode ser menor que 0");
            }

            if (pageSize <= 0) {
                throw new IllegalArgumentException("pageSize deve ser maior que 0");
            }

            List<Bone> bones = repository
                    .findAll()
                    .page(Page.of(page, pageSize))
                    .list();

            List<BoneDTOResponse> response = bones
                .stream()
                .map(BoneDTOResponse::valueOf)
                .toList();

            LOG.infof("%d bonés encontrados", response.size());
            return response;

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os bonés", e);
            throw e;
        }
    }

    @Override
    public List<BoneDTOResponse> findByNome(String nome) {
        LOG.infof("Buscando bonés pelo nome: %s", nome);

        try {

            List<BoneDTOResponse> response = repository
                .find("lower(nome) like lower(?1)", "%" + nome + "%")
                .list()
                .stream()
                .map(BoneDTOResponse::valueOf)
                .toList();

            LOG.infof("Foram encontrados %d bonés", response.size());

            return response;

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar bonés pelo nome: %s", nome);
            throw e;
        }
    }

    @Override
    public BoneDTOResponse findById(Long id) {
        LOG.infof("Buscando boné pelo ID: %d", id);

        try {
            Bone bone = repository.findById(id);

            if (bone == null) {
            LOG.warnf("Boné com ID %d não encontrado", id);
            throw new NotFoundException("Boné não encontrado");
            }

            LOG.infof("Boné com ID %d encontrado", id);

            return BoneDTOResponse.valueOf(bone);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar boné pelo ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public BoneDTOResponse create(BoneDTO dto) {
        LOG.infof("Criando novo boné: %s", dto.nome());
        try {
            Bone bone = new Bone();
            bone.setNome(dto.nome());
            bone.setCor(dto.cor());
            bone.setCategoriaAba(dto.categoriaAba());
            bone.setTamanhoAba(dto.tamanhoAba());
            bone.setProfundidade(dto.profundidade());
            bone.setCircunferencia(dto.circunferencia());
            bone.setBordado(dto.bordado());
            bone.setPreco(dto.preco());

            Material material = repositoryMaterial.findById(dto.idMaterial());
            if (material == null){
                throw new NotFoundException("Material não encontrado");
            }
            
            Marca marca = repositoryMarca.findById(dto.idMarca());
            if (marca == null){
                throw new NotFoundException("Marca não encontrada");
            }

            Modelo modelo = repositoryModelo.findById(dto.idModelo());
            if (modelo == null){
                throw new NotFoundException("Modelo não encontrado");
            }

            bone.setMaterial(material);
            bone.setMarca(marca);
            bone.setModelo(modelo);

            Estoque estoque = new Estoque();
            int quantidade = (dto.quantidadeEstoque() != null) ? dto.quantidadeEstoque() : 0;
            estoque.setQuantidade(quantidade);
            estoque.setDataAtualizacao(LocalDate.now());
            repositoryEstoque.persist(estoque);
            bone.setEstoque(estoque);

            repository.persist(bone);

            if (bone.getEstampas() == null) {
                bone.setEstampas(new ArrayList<>());
            }

            if (dto.idsEstampas() == null || dto.idsEstampas().isEmpty()) {
                bone.getEstampas().clear();
                LOG.debug("Nenhuma estampa foi informada para o boné");
                return BoneDTOResponse.valueOf(bone);
            }

            List<Estampa> estampas = repositoryEstampa.findByIds(dto.idsEstampas());

            if (estampas.size() != dto.idsEstampas().size()) {
                LOG.error("Uma ou mais estampas informadas não existem.");
                throw new IllegalArgumentException("Uma ou mais estampas informadas não existem.");
            }

            bone.setEstampas(estampas);
            LOG.infof("Boné '%s' criado com sucesso com %d estampas", bone.getNome(), estampas.size());
            return BoneDTOResponse.valueOf(bone);

        } catch (Exception e) {
            LOG.error("Erro ao criar boné", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Long id, BoneDTO dto) {

        Bone bone = repository.findById(id);

        if (bone == null) {
            LOG.warnf("Boné com ID %d não encontrado para atualização",         id);
            throw new NotFoundException("Boné não encontrado");
        }

        LOG.infof("Atualizando boné com ID: %d", id);

        try {
            bone.setNome(dto.nome());
            bone.setCor(dto.cor());
            bone.setCategoriaAba(dto.categoriaAba());
            bone.setTamanhoAba(dto.tamanhoAba());
            bone.setProfundidade(dto.profundidade());
            bone.setCircunferencia(dto.circunferencia());
            bone.setBordado(dto.bordado());
            bone.setPreco(dto.preco());

            Material material = repositoryMaterial.findById(dto.idMaterial());
            if (material == null) {
                throw new NotFoundException("Material não encontrado");
            }
            bone.setMaterial(material);

            Marca marca = repositoryMarca.findById(dto.idMarca());
            if (marca == null) {
                throw new NotFoundException("Marca não encontrada");
            }
            bone.setMarca(marca);

            Modelo modelo = repositoryModelo.findById(dto.idModelo());
            if (modelo == null) {
                throw new NotFoundException("Modelo não encontrado");
            }
            bone.setModelo(modelo);

            if (bone.getEstampas() == null) {
                bone.setEstampas(new ArrayList<>());
            }

            if (dto.idsEstampas() == null || dto.idsEstampas().isEmpty()) {
                bone.getEstampas().clear();
                LOG.debug("Limpeza de estampas do boné realizada");
            } else {
                List<Estampa> estampas = repositoryEstampa.findByIds(dto.idsEstampas());
                if (estampas.size() != dto.idsEstampas().size()) {
                    LOG.error("Uma ou mais estampas informadas não existem.");
                    throw new IllegalArgumentException("Uma ou mais estampas informadas não existem.");
                }
                bone.setEstampas(estampas);
                LOG.debugf("Atualizadas %d estampas para o boné", estampas.size());
            }

            Estoque estoque = bone.getEstoque();
            if (estoque == null) {
                estoque = new Estoque();
                int quantidade = (dto.quantidadeEstoque() != null) ? dto.quantidadeEstoque() : 0;
                estoque.setQuantidade(quantidade);
                estoque.setDataAtualizacao(LocalDate.now());
                repositoryEstoque.persist(estoque);
                bone.setEstoque(estoque);
                LOG.debug("Estoque inicial criado para o boné");
            } else {
                if (dto.quantidadeEstoque() != null) {
                    estoque.setQuantidade(dto.quantidadeEstoque());
                }
                estoque.setDataAtualizacao(LocalDate.now());
                LOG.debug("Estoque atualizado para o boné");
            }

            LOG.infof("Boné com ID %d atualizado com sucesso", id);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar boné com ID %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {

        LOG.infof("Deletando boné com ID: %d", id);

        Bone bone = repository.findById(id);

        if (bone == null) {
            LOG.warnf("Boné com ID %d não encontrado para exclusão",    id);
            throw new NotFoundException("Boné não encontrado");
        }

        repository.delete(bone);

        LOG.infof("Boné com ID %d deletado com sucesso", id);
    }

    @Override
    public long count(){
        return repository.count();
    }
}
