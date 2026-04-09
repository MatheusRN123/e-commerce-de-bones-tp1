package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.ModeloDTO;
import br.unitins.topicos1.bone.service.ModeloService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/modelos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModeloResource {

    private static final Logger LOG = Logger.getLogger(ModeloResource.class);

    @Inject
    ModeloService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("pageSize") @DefaultValue("100") int pageSize) {
        LOG.infof("Requisição para buscar modelos recebida [page=%d, pageSize=%d]", page, pageSize);

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

            var modelos = service.findAll(page, pageSize);
            LOG.infof("Retornando %d modelos", modelos.size());
            return Response.ok(modelos).build();

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os modelos", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno do servidor")
                    .build();
        }
    }

    @GET
    @RolesAllowed({"ADM", "USER"})
    @Path("/find/{nome}")
    public Response buscarPorNome(String nome) {
        LOG.infof("Requisição GET /modelos/find/%s", nome);
        Response response = Response.ok(service.findByNome(nome)).build();
        LOG.infof("Resposta enviada para GET /modelos/find/%s", nome);
        return response;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(Long id) {
        LOG.infof("Requisição GET /modelos/%d", id);
        Response response = Response.ok(service.findById(id)).build();
        LOG.infof("Resposta enviada para GET /modelos/%d", id);
        return response;
    }

    @POST
    @RolesAllowed("ADM")
    public Response incluirModelo(ModeloDTO dto) {
        LOG.infof("Requisição POST /modelos - Criando modelo: %s", dto.nome());
        Response response = Response.status(Status.CREATED).entity(service.create(dto)).build();
        LOG.infof("Modelo '%s' criado com sucesso", dto.nome());
        return response;
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterarModelo(Long id, ModeloDTO dto) {
        LOG.infof("Requisição PUT /modelos/%d - Atualizando modelo: %s", id, dto.nome());
        service.update(id, dto);
        LOG.infof("Modelo ID %d atualizado com sucesso", id);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response apagarModelo(Long id) {
        LOG.infof("Requisição DELETE /modelos/%d", id);
        service.delete(id);
        LOG.infof("Modelo ID %d deletado com sucesso", id);
        return Response.noContent().build();
    }
}
