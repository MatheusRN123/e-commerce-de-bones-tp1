package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.EnderecoDTO;
import br.unitins.topicos1.bone.dto.EnderecoDTOResponse;
import br.unitins.topicos1.bone.model.Cidade;
import br.unitins.topicos1.bone.model.Endereco;
import br.unitins.topicos1.bone.model.Usuario;
import br.unitins.topicos1.bone.repository.CidadeRepository;
import br.unitins.topicos1.bone.repository.EnderecoRepository;
import br.unitins.topicos1.bone.repository.PedidoRepository;
import br.unitins.topicos1.bone.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EnderecoServiceImpl implements EnderecoService {

    private static final Logger LOG = Logger.getLogger(EnderecoServiceImpl.class);

    @Inject
    JsonWebToken jwt;

    @Inject
    EnderecoRepository repository;
    
    @Inject
    CidadeRepository cidadeRepository;

    @Inject
    PedidoRepository pedidoRepository;

    @Inject 
    UsuarioRepository repositoryUsuario;

    @Override
    public List<EnderecoDTOResponse> findAll() {
        String login = jwt.getSubject();
        Usuario usuario = repositoryUsuario.findByLogin(login);

        LOG.info("Buscando todos os endereços");
        try {
            List<Endereco> lista;

            if(isAdmin()){
                LOG.info("Usuário é ADMIN, retornando todos os endereços");
                lista = repository.listAll();
            }else{
                LOG.infof("Usuário é CLIENTE (%s), retornando apenas seus endereços", login);
                lista = repository.find("usuario", usuario).list();
            }

            List<EnderecoDTOResponse> response = lista
                    .stream()
                    .map(EnderecoDTOResponse::valueOf)
                    .toList();

            LOG.infof("%d endereços encontrados", response.size());
            return response;
        } catch (Exception e) {
            LOG.error("Erro ao buscar endereços", e);
            throw e;
        }
    }

    @Override
    public EnderecoDTOResponse findById(Long id) {
        String login = jwt.getSubject();
        Usuario usuario = repositoryUsuario.findByLogin(login);

        LOG.infof("Buscando endereço pelo ID: %d", id);

        try {
            Endereco endereco;

            if (isAdmin()) {
                LOG.info("Usuário é ADMIN, buscando endereço sem restrição");
                endereco = repository.findById(id);
            }else{
                LOG.infof("Usuário é CLIENTE (%s), buscando apenas seu endereço", login);
                endereco = repository
                        .find("id = ?1 and usuario = ?2", id, usuario)
                        .firstResult();
            }

            if (endereco == null) {
                LOG.warnf("Endereço com ID %d não encontrado para o usuário %s", id, login);
                throw new NotFoundException("Endereço não encontrado");
            }

            LOG.infof("Endereço com ID %d encontrado para o usuário %s", id, login);

            return EnderecoDTOResponse.valueOf(endereco);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar endereço pelo ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public EnderecoDTOResponse create(EnderecoDTO dto) {
        String login = jwt.getSubject();
        Usuario usuario = repositoryUsuario.findByLogin(login);

        LOG.infof("Criando endereço: %s, CEP: %s", dto.logradouro(), dto.cep());
        try {
            Endereco endereco = new Endereco();
            endereco.setNomeDestinatario(dto.nomeDestinatario());
            endereco.setCep(dto.cep());
            endereco.setLogradouro(dto.logradouro());
            endereco.setNumero(dto.numero());
            endereco.setUsuario(usuario);

            Cidade cidade = cidadeRepository.findById(dto.idCidade());
            endereco.setCidade(cidade);

            repository.persist(endereco);

            LOG.infof("Endereço criado com sucesso: %s, ID: %d", dto.logradouro(), endereco.getId());
            return EnderecoDTOResponse.valueOf(endereco);
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar endereço: %s", dto.logradouro());
            throw e;
        }
    }

    @Override
    @Transactional
    public void update(Long id, EnderecoDTO dto) {

        String login = jwt.getSubject();
        Usuario usuario = repositoryUsuario.findByLogin(login);

        LOG.infof("Atualizando endereço ID: %d", id);

        try {
            Endereco endereco;

            if (isAdmin()) {
                LOG.info("Usuário é ADMIN, pode atualizar qualquer          endereço");
                endereco = repository.findById(id);
            } else {
                LOG.infof("Usuário é CLIENTE (%s), pode atualizar apenas seu            endereço", login);
                endereco = repository
                        .find("id = ?1 and usuario = ?2", id, usuario)
                        .firstResult();
            }

            if (endereco == null) {
                LOG.warnf("Endereço ID %d não encontrado para o     usuário %s", id, login);
                throw new NotFoundException("Endereço não   encontrado");
            }

            endereco.setNomeDestinatario(dto.nomeDestinatario());
            endereco.setCep(dto.cep());
            endereco.setLogradouro(dto.logradouro());
            endereco.setNumero(dto.numero());

            Cidade cidade = cidadeRepository.findById(dto.idCidade());
            endereco.setCidade(cidade);

            LOG.infof("Endereço ID %d atualizado com sucesso", id);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar endereço ID: %d", id);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {

        String login = jwt.getSubject();
        Usuario usuario = repositoryUsuario.findByLogin(login);

        LOG.infof("Deletando endereço ID: %d", id);

        try {
            Endereco endereco;

            if (isAdmin()) {
                LOG.info("Usuário é ADMIN, pode deletar qualquer endereço");
                endereco = repository.findById(id);
            } else {
                LOG.infof("Usuário é CLIENTE (%s), pode deletar apenas seu          endereço", login);
                endereco = repository
                        .find("id = ?1 and usuario = ?2", id, usuario)
                        .firstResult();
            }

            if (endereco == null) {
                LOG.warnf("Endereço ID %d não encontrado para o     usuário %s", id, login);
                throw new NotFoundException("Endereço não   encontrado");
            }

            repository.delete(endereco);

            LOG.infof("Endereço ID %d deletado com sucesso", id);

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar endereço ID: %d", id);
            throw e;
        }
    }

    private boolean isAdmin() {
        return jwt.getGroups().contains("ADM");
    }
}
