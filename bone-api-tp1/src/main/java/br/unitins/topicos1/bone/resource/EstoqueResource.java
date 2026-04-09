package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.EstoqueDTO;
import br.unitins.topicos1.bone.dto.EstoqueDTOResponse;
import br.unitins.topicos1.bone.service.EstoqueService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/estoques")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstoqueResource {

    private static final Logger LOG = Logger.getLogger(EstoqueResource.class);

    @Inject
    EstoqueService service;

    @GET
    @RolesAllowed("ADM")
    public Response BuscarTodos(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("pageSize") @DefaultValue("100") int pageSize) {
        LOG.infof("Requisição para buscar estoques recebida [page=%d, pageSize=%d]", page, pageSize);

        try {
            if (page < 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("Page não pode ser menor que 0")
                        .build();
            }

            if (pageSize <= 0) {
                return Response.status(Status.BAD_REQUEST)
                        .entity("pageSize deve ser maior que 0")
                        .build();
            }

            if (pageSize > 100) {
                pageSize = 100;
            }

            var estoques = service.findAll(page, pageSize);
            LOG.infof("Retornando %d estoques", estoques.size());
            return Response.ok(estoques).build();

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os estoques", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno do servidor")
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response buscarPorId(@PathParam("id") Long id) {

        LOG.infof("Requisição para buscar estoque por ID: %d", id);
        try {
            EstoqueDTOResponse estoque = service.findById(id);

            if (estoque == null) {
                LOG.warnf("Estoque ID %d não encontrado", id);
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(estoque).build();

        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estoque ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();

        }
    }

    @GET
    @Path("/{id}/disponibilidade")
    @RolesAllowed("ADM")
    public Response verificarDisponibilidade(@PathParam("id") Long id) {
        LOG.infof("Requisição para verificar disponibilidade do estoque ID: %d", id);
        try {
            var disponivel = service.verificarDisponibilidade(id);
            LOG.infof("Estoque ID %d disponível: %b", id, disponivel);
            return Response.ok(disponivel).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao verificar disponibilidade do estoque ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/quantidade")
    @RolesAllowed("ADM")
    public Response atualizarQuantidade(@PathParam("id") Long id, EstoqueDTO dto) {
        LOG.infof("Requisição para atualizar quantidade do estoque ID: %d", id);
        try {
            service.atualizarQuantidade(id, dto);
            LOG.infof("Quantidade do estoque ID %d atualizada", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar quantidade do estoque ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}/adicionar")
    @RolesAllowed("ADM")
    public Response adicionarQuantidade(@PathParam("id") Long id, EstoqueDTO dto) {
        LOG.infof("Requisição para adicionar quantidade ao estoque ID: %d", id);
        try {
            service.adicionarQuantidade(id, dto);
            LOG.infof("Quantidade adicionada ao estoque ID %d com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao adicionar quantidade ao estoque ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
