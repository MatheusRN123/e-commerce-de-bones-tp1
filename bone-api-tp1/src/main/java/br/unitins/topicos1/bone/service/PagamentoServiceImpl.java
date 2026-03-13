package br.unitins.topicos1.bone.service;

import java.util.List;

import br.unitins.topicos1.bone.dto.PagamentoDTOResponse;
import br.unitins.topicos1.bone.model.Pagamento;
import br.unitins.topicos1.bone.model.Usuario;
import br.unitins.topicos1.bone.repository.PagamentoRepository;
import br.unitins.topicos1.bone.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PagamentoServiceImpl implements PagamentoService {

    private static final Logger LOG = Logger.getLogger(PagamentoServiceImpl.class);

    @Inject
    JsonWebToken jwt;

    @Inject
    PagamentoRepository pagamentoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Override
    public List<PagamentoDTOResponse> findAll() {

        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Buscando pagamentos para usuário %s", login);

        List<Pagamento> lista;

        if (isAdmin()) {
            LOG.info("Usuário é ADMIN, retornando todos os  pagamentos");
            lista = pagamentoRepository.listAll();
        } else {
            LOG.info("Usuário é CLIENTE, retornando apenas seus     pagamentos");

            lista = pagamentoRepository
                    .find("pedido.usuario", usuario)
                    .list();
        }

        return lista
                .stream()
                .map(PagamentoDTOResponse::valueOf)
                .toList();
    }

    @Override
    public PagamentoDTOResponse findById(Long id) {

        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Buscando pagamento ID %d para usuário %s", id,   login);

        Pagamento pagamento;

        if (isAdmin()) {
            LOG.info("Usuário é ADMIN, buscando pagamento sem   restrição");
            pagamento = pagamentoRepository.findById(id);
        } else {
            LOG.info("Usuário é CLIENTE, buscando apenas seu    pagamento");

            pagamento = pagamentoRepository
                    .find("id = ?1 and pedido.usuario = ?2", id,    usuario)
                    .firstResult();
        }

        if (pagamento == null) {
            LOG.warnf("Pagamento %d não encontrado para usuário %s",    id, login);
            return null;
        }

        return PagamentoDTOResponse.valueOf(pagamento);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, String novoStatus) {
        LOG.infof("Atualizando status do pagamento com ID %d para '%s'", id, novoStatus);
        
        Pagamento pagamento = pagamentoRepository.findById(id);
        if (pagamento == null) {
            LOG.warnf("Pagamento com ID %d não encontrado", id);
            throw new NotFoundException("Pagamento não encontrado");
        }

        if (!"PAGO".equalsIgnoreCase(novoStatus) && !"PENDENTE".equalsIgnoreCase(novoStatus)) {
            LOG.warnf("Status inválido: %s", novoStatus);
            throw new IllegalArgumentException("Status deve ser 'PAGO' ou 'PENDENTE'");
        }

        pagamento.setStatus(novoStatus.toUpperCase());
        pagamentoRepository.persist(pagamento);

        LOG.infof("Pagamento com ID %d atualizado para status '%s'", id, pagamento.getStatus());
    }

    private boolean isAdmin() {
        return jwt.getGroups().contains("ADM");
    }
}
