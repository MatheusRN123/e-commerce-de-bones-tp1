package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.MaterialDTO;
import br.unitins.topicos1.bone.service.MaterialService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.jboss.logging.Logger;

@Path("/materiais")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MaterialResource {

    private static final Logger LOG = Logger.getLogger(MaterialResource.class);

    @Inject
    MaterialService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos(@QueryParam("page") @DefaultValue("0") int page,
                                @QueryParam("pageSize") @DefaultValue("100") int pageSize) {
        LOG.infof("Requisição para buscar materiais recebida [page=%d, pageSize=%d]", page, pageSize);

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

            var materiais = service.findAll(page, pageSize);
            LOG.infof("Retornando %d materiais", materiais.size());
            return Response.ok(materiais).build();

        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os materiais", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro interno do servidor")
                    .build();
        }
    }

    @GET
    @Path("/find/{nome}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorNome(String nome) {
        LOG.infof("Requisição para buscar materiais pelo nome: %s", nome);
        Response response = Response.ok(service.findByNome(nome)).build();
        LOG.infof("Resposta enviada para materiais com nome: %s", nome);
        return response;
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(Long id) {
        LOG.infof("Requisição para buscar material pelo ID: %d", id);
        Response response = Response.ok(service.findById(id)).build();
        LOG.infof("Resposta enviada para material ID: %d", id);
        return response;
    }

    @POST
    @RolesAllowed("ADM")
    public Response incluirMaterial(MaterialDTO dto) {
        LOG.infof("Requisição para criar material: %s", dto.nome());
        Response response = Response.status(Status.CREATED).entity(service.create(dto)).build();
        LOG.infof("Material '%s' criado com sucesso", dto.nome());
        return response;
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterarMaterial(Long id, MaterialDTO dto) {
        LOG.infof("Requisição para atualizar material ID: %d", id);
        service.update(id, dto);
        LOG.infof("Material ID %d atualizado com sucesso", id);
        return Response.noContent().build();
    }

    @GET
    @Path("/count")
    public Response count() {
        LOG.info("Requisição para contar materiais");
        long count = service.count();
        LOG.infof("Total de materiais: %d", count);
        return Response.ok(count).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response apagarMaterial(Long id) {
        LOG.infof("Requisição para deletar material ID: %d", id);
        service.delete(id);
        LOG.infof("Material ID %d deletado com sucesso", id);
        return Response.noContent().build();
    }
}
