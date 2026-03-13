package br.unitins.topicos1.bone.resources;

import br.unitins.topicos1.bone.dto.EnderecoDTO;
import br.unitins.topicos1.bone.dto.EnderecoDTOResponse;
import br.unitins.topicos1.bone.resource.EnderecoResource;
import br.unitins.topicos1.bone.service.EnderecoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
class EnderecoResourceUnitTest {

    @Inject
    EnderecoResource enderecoResource;

    @InjectMock
    EnderecoService enderecoService;

    @Test
    void testBuscarTodos() {
        EnderecoDTOResponse dto1 = mock(EnderecoDTOResponse.class);
        EnderecoDTOResponse dto2 = mock(EnderecoDTOResponse.class);
        when(enderecoService.findAll()).thenReturn(List.of(dto1, dto2));

        Response response = enderecoResource.buscarTodos();

        assertEquals(200, response.getStatus());
        verify(enderecoService).findAll();
    }

    @Test
    void testBuscarPorIdExistente() {
        Long id = 1L;
        EnderecoDTOResponse dto = mock(EnderecoDTOResponse.class);
        when(enderecoService.findById(id)).thenReturn(dto);

        Response response = enderecoResource.buscarPorId(id);

        assertEquals(200, response.getStatus());
        assertEquals(dto, response.getEntity());
        verify(enderecoService).findById(id);
    }

    @Test
    void testBuscarPorIdInexistente() {
        Long id = 999L;
        when(enderecoService.findById(id)).thenReturn(null);

        Response response = enderecoResource.buscarPorId(id);

        assertEquals(404, response.getStatus());
        verify(enderecoService).findById(id);
    }

    @Test
    void testIncluirEndereco() {
        EnderecoDTO dto = new EnderecoDTO("Aas", "12345678", "Rua Teste", "10", 1L);
        EnderecoDTOResponse dtoResponse = mock(EnderecoDTOResponse.class);

        when(enderecoService.create(dto)).thenReturn(dtoResponse);

        Response response = enderecoResource.incluirEndereco(dto);

        assertEquals(201, response.getStatus());
        assertEquals(dtoResponse, response.getEntity());
        verify(enderecoService).create(dto);
    }

    @Test
    void testAlterarEndereco() {
        Long id = 1L;
        EnderecoDTO dto = new EnderecoDTO("asdasd", "87654321", "Rua Atualizada", "15", 1L);

        doNothing().when(enderecoService).update(id, dto);

        Response response = enderecoResource.alterarEndereco(id, dto);

        assertEquals(204, response.getStatus());
        verify(enderecoService).update(id, dto);
    }

    @Test
    void testDeletarEndereco() {
        Long id = 1L;

        doNothing().when(enderecoService).delete(id);

        Response response = enderecoResource.deletarEndereco(id);

        assertEquals(204, response.getStatus());
        verify(enderecoService).delete(id);
    }
}
