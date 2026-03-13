package br.unitins.topicos1.bone.service;

import java.time.LocalDateTime;
import java.util.List;

import br.unitins.topicos1.bone.dto.PedidoDTO;
import br.unitins.topicos1.bone.dto.PedidoDTOResponse;
import br.unitins.topicos1.bone.dto.PagamentoDTO;
import br.unitins.topicos1.bone.dto.BoletoDTO;
import br.unitins.topicos1.bone.dto.CartaoDTO;
import br.unitins.topicos1.bone.dto.ItemPedidoDTO;
import br.unitins.topicos1.bone.model.*;
import br.unitins.topicos1.bone.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

@ApplicationScoped
public class PedidoServiceImpl implements PedidoService {

    private static final Logger LOG = Logger.getLogger(PedidoServiceImpl.class);

    @Inject
    JsonWebToken jwt;

    @Inject
    PedidoRepository repository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    BoneRepository boneRepository;

    @Override
    public List<PedidoDTOResponse> findAll() {
        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Buscando pedidos para usuário %s", login);

        List<Pedido> lista;

        if (isAdmin()) {
            LOG.info("Usuário é ADMIN, retornando todos os  pedidos");
            lista = repository.listAll();
        } else {
            LOG.info("Usuário é CLIENTE, retornando apenas seus     pedidos");
            lista = repository.find("usuario", usuario).list();
        }

        List<PedidoDTOResponse> pedidos = lista
                .stream()
                .map(PedidoDTOResponse::valueOf)
                .toList();

        LOG.infof("Encontrados %d pedidos", pedidos.size());

        return pedidos;
    }

    @Override
    public PedidoDTOResponse findById(Long id) {
        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Buscando pedido ID %d para usuário %s", id,  login);

        Pedido pedido;

        if (isAdmin()) {
            LOG.info("Usuário é ADMIN, buscando pedido sem restrição");
            pedido = repository.findById(id);
        } else {
            LOG.info("Usuário é CLIENTE, buscando apenas seu pedido");
            pedido = repository
                    .find("id = ?1 and usuario = ?2", id, usuario)
                    .firstResult();
        }

        if (pedido == null) {
            LOG.warnf("Pedido %d não encontrado para usuário %s",   id, login);
            return null;
        }

        return PedidoDTOResponse.valueOf(pedido);
    }

    @Override
    @Transactional
    public PedidoDTOResponse create(PedidoDTO dto) {
        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Criando pedido para usuário %s", login);

        Pedido pedido = new Pedido();
        pedido.setData(LocalDateTime.now());
        pedido.setUsuario(usuario);

        Endereco endereco = enderecoRepository.findById(dto.idEndereco());

        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não encontrado");
        }

        pedido.setCep(endereco.getCep());
        pedido.setLogradouro(endereco.getLogradouro());
        pedido.setNumero(endereco.getNumero());
        pedido.setCidade(endereco.getCidade().getNome());
        pedido.setEstado(endereco.getCidade().getEstado().getNome());

        List<ItemPedido> itens = dto.itens()
                .stream()
                .map(i -> criarItem(i, pedido))
                .toList();

        pedido.setItens(itens);

        Pagamento pagamento = criarPagamento(dto.pagamento(), pedido);
        pedido.setPagamento(pagamento);

        repository.persist(pedido);

        LOG.infof("Pedido criado com sucesso. ID: %d", pedido.getId ());

        return PedidoDTOResponse.valueOf(pedido);
    }

    @Override
    @Transactional
    public void update(Long id, PedidoDTO dto) {
        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Atualizando pedido ID: %d para usuário %s", id,  login);

        Pedido pedido;

        if (isAdmin()) {
            LOG.info("Usuário é ADMIN, pode atualizar qualquer pedido");
            pedido = repository.findById(id);
        } else {
            LOG.info("Usuário é CLIENTE, pode atualizar apenas seu      pedido");
            pedido = repository
                    .find("id = ?1 and usuario = ?2", id, usuario)
                    .firstResult();
        }

        if (pedido == null) {
            LOG.warnf("Pedido com ID %d não encontrado para o   usuário %s", id, login);
            return;
        }

        pedido.getItens().clear();

        for (ItemPedidoDTO itemDTO : dto.itens()) {
            ItemPedido item = criarItem(itemDTO, pedido);
            pedido.getItens().add(item);
        }

        Pagamento pagamento = criarPagamento(dto.pagamento(), pedido);
        pedido.setPagamento(pagamento);

        LOG.infof("Pedido ID %d atualizado com sucesso", id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        String login = jwt.getSubject();
        Usuario usuario = usuarioRepository.findByLogin(login);

        LOG.infof("Deletando pedido ID: %d do usuário %s", id,  login);

        Pedido pedido;

    if (isAdmin()) {
        LOG.info("Usuário é ADMIN, pode deletar qualquer pedido");
        pedido = repository.findById(id);
    } else {
        LOG.info("Usuário é CLIENTE, pode deletar apenas seu    pedido");
        pedido = repository
                .find("id = ?1 and usuario = ?2", id, usuario)
                .firstResult();
    }

        if (pedido == null) {
            LOG.warnf("Pedido com ID %d não encontrado para o   usuário %s", id, login);
            return;
        }

        repository.delete(pedido);

        LOG.infof("Pedido ID %d deletado com sucesso", id);
    }

    private Pagamento criarPagamento(PagamentoDTO dto,  Pedido pedido) {
        if (dto == null)
            throw new IllegalArgumentException("Pagamento não informado");
    
        LOG.infof("Criando pagamento do tipo %s para usuário %s", dto.tipoPagamento(), jwt.getSubject());
    
        Pagamento pagamento;
    
        switch (dto.tipoPagamento().toUpperCase()) {
            case "PIX" -> {
                Pix pix = new Pix();
                pix.setChave(dto.pix().chave());
                pix.setTipoChave(dto.pix().tipoChave());
                pagamento = pix;
            }
            case "CARTAO" -> {
                Cartao cartao = new Cartao();
                CartaoDTO c = dto.cartao();
                cartao.setNomeTitular(c.nomeTitular());
                cartao.setNumero(c.numero());
                cartao.setValidade(c.validade());
                cartao.setCvv(c.cvv());
                pagamento = cartao;
            }
            case "BOLETO" -> {
                Boleto boleto = new Boleto();
                BoletoDTO b = dto.boleto();
                boleto.setCodigoBarras(b.codigoBarras());
                boleto.setDataVencimento(b.dataVencimento());
                pagamento = boleto;
            }
            default -> throw new IllegalArgumentException   ("Tipo de pagamento inválido: " + dto. tipoPagamento());
        }
    
        pagamento.setPedido(pedido);
        pagamento.setData(LocalDateTime.now());
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setStatus("PENDENTE");
    
        return pagamento;
    }


    private ItemPedido criarItem(ItemPedidoDTO dto, Pedido pedido) {
        LOG.infof("Criando item de pedido para Bone ID %d, quantidade %d", dto.idBone(), dto.quantidade());

        Bone bone = boneRepository.findById(dto.idBone());

        if(bone == null){
            throw new IllegalArgumentException("Boné não encontrado");
        }

        Estoque estoque = bone.getEstoque();

        if(estoque == null){
            throw new IllegalStateException("Boné não possui estoque cadastrado");
        }

        if(estoque.getQuantidade() < dto.quantidade()){
            throw new IllegalStateException("Estoque insuficiente para o boné: " + bone.getNome());
        }

        estoque.atualizarQuantidade(estoque.getQuantidade() - dto.quantidade());

        ItemPedido item = new ItemPedido();
        item.setBone(bone);
        item.setQuantidade(dto.quantidade());
        item.setPreco(bone.getPreco());
        item.setPedido(pedido);

        LOG.infof(
            "Item criado. Boné: %s | Quantidade: %d | Estoque restante: %d",
            bone.getNome(),
            dto.quantidade(),
            estoque.getQuantidade()
        );

        return item;
    }

    private boolean isAdmin() {
        return jwt.getGroups().contains("ADM");
    }
}
