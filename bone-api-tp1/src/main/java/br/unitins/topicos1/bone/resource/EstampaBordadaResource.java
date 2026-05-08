package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.EstampaBordadaDTO;
import br.unitins.topicos1.bone.service.EstampaBordadaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/estampas/bordada")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstampaBordadaResource {

    private static final Logger LOG = Logger.getLogger(EstampaBordadaResource.class);

    @Inject
    EstampaBordadaService service;

    @POST
    @RolesAllowed("ADM")
    public Response incluir(EstampaBordadaDTO dto) {
        LOG.infof("Requisição para criar estampa bordada: %s", dto.nome());
        try {
            var response = service.create(dto);
            LOG.infof("Estampa bordada '%s' criada com sucesso", dto.nome());
            return Response.status(Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar estampa bordada: %s", dto.nome());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterar(Long id, EstampaBordadaDTO dto) {
        LOG.infof("Requisição para atualizar estampa bordada ID: %d", id);
        try {
            service.update(id, dto);
            LOG.infof("Estampa bordada ID %d atualizada com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar estampa bordada ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
