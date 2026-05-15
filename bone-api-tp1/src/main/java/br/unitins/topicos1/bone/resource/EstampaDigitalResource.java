package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.EstampaDigitalDTO;
import br.unitins.topicos1.bone.service.EstampaDigitalService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/estampas/digital")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstampaDigitalResource {

    private static final Logger LOG = Logger.getLogger(EstampaDigitalResource.class);

    @Inject
    EstampaDigitalService service;

    @POST
    @RolesAllowed("ADM")
    public Response incluir(EstampaDigitalDTO dto) {
        LOG.infof("Requisição para criar estampa digital: %s", dto.nome());
        try {
            var response = service.create(dto);
            LOG.infof("Estampa digital '%s' criada com sucesso", dto.nome());
            return Response.status(Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar estampa digital: %s", dto.nome());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterar(Long id, EstampaDigitalDTO dto) {
        LOG.infof("Requisição para atualizar estampa digital ID: %d", id);
        try {
            service.update(id, dto);
            LOG.infof("Estampa digital ID %d atualizada com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar estampa digital ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
