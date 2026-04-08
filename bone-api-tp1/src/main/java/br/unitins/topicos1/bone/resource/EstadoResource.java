package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.EstadoDTO;
import br.unitins.topicos1.bone.dto.EstadoDTOResponse;
import br.unitins.topicos1.bone.service.EstadoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/estados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstadoResource {

    private static final Logger LOG = Logger.getLogger(EstadoResource.class);

    @Inject
    EstadoService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos() {
        LOG.info("Requisição para buscar todos os estados recebida");
        try {
            var estados = service.findAll();
            LOG.infof("Retornando %d estados", estados.size());
            return Response.ok(estados).build();
        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os estados", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/find/{sigla}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorSigla(@PathParam("sigla") String sigla) {
        LOG.infof("Requisição para buscar estados pela sigla: %s", sigla);
        try {
            var estados = service.findBySigla(sigla);
            LOG.infof("Encontrados %d estados com a sigla '%s'", estados.size(), sigla);
            return Response.ok(estados).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estados pela sigla: %s", sigla);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(Long id) {
        LOG.infof("Requisição para buscar estado pelo ID: %d", id);
        try {
            EstadoDTOResponse response = service.findById(id);
            if (response == null) {
                LOG.warnf("Estado com ID %d não encontrado", id);
                return Response.status(Status.NOT_FOUND).build();
            }
            LOG.infof("Estado com ID %d encontrado: %s", id, response.nome());
            return Response.ok(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar estado pelo ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @RolesAllowed("ADM")
    public Response incluirEstado(@Valid EstadoDTO dto) {
        LOG.infof("Requisição para criar estado: %s (%s)", dto.nome(), dto.sigla());
        try {
            var response = service.create(dto);
            LOG.infof("Estado '%s' criado com sucesso", dto.nome());
            return Response.status(Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar estado: %s (%s)", dto.nome(), dto.sigla());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterarEstado(Long id, @Valid EstadoDTO dto) {
        LOG.infof("Requisição para atualizar estado ID: %d", id);
        try {
            service.update(id, dto);
            LOG.infof("Estado ID %d atualizado com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar estado ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response deletarEstado(Long id) {
        LOG.infof("Requisição para deletar estado ID: %d", id);
        try {
            service.delete(id);
            LOG.infof("Estado ID %d deletado com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar estado ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
