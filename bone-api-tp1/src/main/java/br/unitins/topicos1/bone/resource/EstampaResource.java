package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.service.EstampaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/estampas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstampaResource {

    private static final Logger LOG = Logger.getLogger(EstampaResource.class);

    @Inject
    EstampaService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("pageSize") @DefaultValue("100") int pageSize) {
        LOG.infof("Requisição para buscar estampas recebida [page=%d, pageSize=%d]", page, pageSize);

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

            var estampas = service.findAll(page, pageSize);
            LOG.infof("Retornando %d estampas", estampas.size());
            return Response.ok(estampas).build();

        } catch (Exception e) {
            LOG.error("Erro ao buscar todas as estampas", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno do servidor")
                    .build();
        }
    }

    @GET
    @Path("/find/{nome}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorNome(@PathParam("nome") String nome) {
        LOG.infof("Requisição para buscar estampas pelo nome: %s", nome);
        try {
            var response = service.findByNome(nome);
            LOG.infof("Encontradas %d estampas com o nome '%s'", response.size(), nome);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estampas pelo nome: %s", nome);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Requisição para buscar estampa pelo ID: %d", id);
        try {
            var response = service.findById(id);
            LOG.infof("Estampa ID %d encontrada", id);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estampa pelo ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/count")
    public Response count() {
        LOG.info("Requisição para contar estampas");
        long count = service.count();
        LOG.infof("Total de estampas: %d", count);
        return Response.ok(count).build();
    }
}
