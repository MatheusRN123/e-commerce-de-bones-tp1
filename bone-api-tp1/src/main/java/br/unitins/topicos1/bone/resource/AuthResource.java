package br.unitins.topicos1.bone.resource;

import br.unitins.topicos1.bone.dto.AuthDTO;
import br.unitins.topicos1.bone.model.Usuario;
import br.unitins.topicos1.bone.service.HashService;
import br.unitins.topicos1.bone.service.JwtService;
import br.unitins.topicos1.bone.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    HashService hashService;

    @Inject
    JwtService jwtService;

    @Inject
    UsuarioService usuarioService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(AuthDTO dto) {

        Usuario usuario = usuarioService.findByLoginAndSenha(dto.login(), dto.senha());

        if (usuario == null)
        return Response.status(Status.UNAUTHORIZED).entity("Login ou senha inválidos").build();

        
        String token = jwtService.generateJwt(usuario.getLogin(), usuario.getPerfil());

        return Response.ok()
            .header("Authorization", "Bearer " + token)
            .build();
    }
    
}