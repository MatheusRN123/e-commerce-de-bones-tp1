package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.BoneDTO;
import br.unitins.topicos1.bone.dto.BoneDTOResponse;
import br.unitins.topicos1.bone.service.BoneService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.logging.Logger;

@Path("/bones")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoneResource {
    
    private static final Logger LOG = Logger.getLogger(BoneResource.class);

    @Inject
    BoneService service;

    @GET
    @RolesAllowed({"ADM", "USER"})
    public Response buscarTodos() {
        LOG.info("Requisição para buscar todos os bonés recebida");
        try {
            var bones = service.findAll();
            LOG.infof("Retornando %d bonés", bones.size());
            return Response.ok(bones).build();
        } catch (Exception e) {
            LOG.error("Erro ao buscar todos os bonés", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/find/{nome}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorNome(@PathParam("nome") String nome) {
        LOG.infof("Requisição para buscar bonés pelo nome: %s", nome);
        try {
            var bones = service.findByNome(nome);
            LOG.infof("Encontrados %d bonés com o nome '%s'", bones.size(), nome);
            return Response.ok(bones).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar bonés pelo nome: %s", nome);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"ADM", "USER"})
    public Response buscarPorId(@PathParam("id") Long id) {
        LOG.infof("Requisição para buscar boné pelo ID: %d", id);
        try {
            BoneDTOResponse response = service.findById(id);
            return Response.ok(response).build();
        
        } catch (NotFoundException e) {
            LOG.warnf("Boné com ID %d não encontrado", id);
            return Response.status(Status.NOT_FOUND).build();
        
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao buscar boné pelo ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @RolesAllowed("ADM")
    public Response incluirBone(BoneDTO dto) {
        LOG.infof("Requisição para criar boné: %s", dto.nome());
        try {
            BoneDTOResponse response = service.create(dto);
            LOG.infof("Boné '%s' criado com sucesso", dto.nome());
            return Response.status(Status.CREATED).entity(response).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao criar boné: %s", dto.nome());
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response alterarBone(@PathParam("id") Long id, BoneDTO dto) {
        LOG.infof("Requisição para atualizar boné ID: %d", id);
        try {
            service.update(id, dto);
            LOG.infof("Boné ID %d atualizado com sucesso", id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            LOG.warnf("Boné ID %d não encontrado para atualização", id);
            return Response.status(Status.NOT_FOUND).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao atualizar boné ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("ADM")
    public Response deletarBone(@PathParam("id") Long id) {
        LOG.infof("Requisição para deletar boné ID: %d", id);
        try {
            service.delete(id);
            LOG.infof("Boné ID %d deletado com sucesso", id);
            return Response.noContent().build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro ao deletar boné ID: %d", id);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
