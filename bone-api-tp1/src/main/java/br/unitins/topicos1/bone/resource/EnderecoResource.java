package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.EnderecoDTO;
import br.unitins.topicos1.bone.dto.EnderecoDTOResponse;
import br.unitins.topicos1.bone.service.EnderecoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
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

@Path("/enderecos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    private static final Logger LOG = Logger.getLogger(EnderecoResource.class);

    @Inject
    EnderecoService service;

    @GET
    @RolesAllowed({"ADM","USER"})
    public Response buscarTodos() {
        LOG.info("Requisição para buscar todos os endereços recebida");
        try {
            var enderecos = service.findAll();
            LOG.infof("Retornando %d endereços", enderecos.size());
            return Response.ok(enderecos).build();
        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os endereços", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM","USER"})
    public Response buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Requisição para buscar endereço pelo ID: %d", id);
        try {
            EnderecoDTOResponse response = service.findById(id);
            if (response == null) {
                LOG.warnf("Endereço com ID %d não encontrado", id);
                return Response.status(Status.NOT_FOUND).build();
            }
            LOG.infof("Endereço com ID %d encontrado: %s", id, response.logradouro());
            return Response.ok(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar endereço pelo ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @RolesAllowed({"ADM","USER"})
    public Response incluirEndereco(EnderecoDTO dto) {
        LOG.infof("Requisição para criar endereço: %s, CEP: %s", dto.logradouro(), dto.cep());
        try {
            EnderecoDTOResponse response = service.create(dto);
            LOG.infof("Endereço '%s' criado com sucesso", dto.logradouro());
            return Response.status(Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar endereço: %s", dto.logradouro());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed({"ADM","USER"})
    public Response alterarEndereco(@PathParam("id") Long id, EnderecoDTO dto) {
        LOG.infof("Requisição para atualizar endereço ID: %d", id);
        try {
            service.update(id, dto);
            LOG.infof("Endereço ID %d atualizado com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar endereço ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed({"ADM","USER"})
    public Response deletarEndereco(@PathParam("id") Long id) {
        LOG.infof("Requisição para deletar endereço ID: %d", id);
        try {
            service.delete(id);
            LOG.infof("Endereço ID %d deletado com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar endereço ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}
