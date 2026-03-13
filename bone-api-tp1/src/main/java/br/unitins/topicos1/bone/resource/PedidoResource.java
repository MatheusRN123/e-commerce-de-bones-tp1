package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.PedidoDTO;
import br.unitins.topicos1.bone.dto.PedidoDTOResponse;
import br.unitins.topicos1.bone.service.PedidoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/pedidos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PedidoResource {

    private static final Logger LOG = Logger.getLogger(PedidoResource.class);

    @Inject
    PedidoService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos() {
        LOG.info("Requisição recebida: buscar todos os pedidos");
        var pedidos = service.findAll();
        LOG.infof("Retornando %d pedidos", pedidos.size());
        return Response.ok(pedidos).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Requisição recebida: buscar pedido por ID %d", id);
        PedidoDTOResponse response = service.findById(id);
        if (response == null) {
            LOG.warnf("Pedido com ID %d não encontrado", id);
            return Response.status(Status.NOT_FOUND).build();
        }
        LOG.infof("Pedido encontrado: ID %d", id);
        return Response.ok(response).build();
    }

    @POST
    @RolesAllowed("USER")
    public Response incluirPedido(PedidoDTO dto) {
        LOG.info("Requisição recebida: criar pedido");
        PedidoDTOResponse response = service.create(dto);
        LOG.infof("Pedido criado com sucesso: ID %d", response.id());
        return Response.status(Status.CREATED).entity(response).build();
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM","USER"})
    public Response alterarPedido(@PathParam("id") Long id, PedidoDTO dto) {
        LOG.infof("Requisição recebida: atualizar pedido ID %d", id);
        service.update(id, dto);
        LOG.infof("Pedido ID %d atualizado com sucesso", id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM","USER"})
    public Response deletarPedido(@PathParam("id") Long id) {
        LOG.infof("Requisição recebida: deletar pedido ID %d", id);
        service.delete(id);
        LOG.infof("Pedido ID %d deletado com sucesso", id);
        return Response.noContent().build();
    }
}
