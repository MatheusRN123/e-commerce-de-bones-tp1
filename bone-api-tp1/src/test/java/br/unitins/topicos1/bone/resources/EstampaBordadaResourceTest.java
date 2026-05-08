package br.unitins.topicos1.bone.resources;

import br.unitins.topicos1.bone.dto.EstampaBordadaDTO;
import br.unitins.topicos1.bone.dto.EstampaBordadaDTOResponse;
import br.unitins.topicos1.bone.resource.EstampaBordadaResource;
import br.unitins.topicos1.bone.service.EstampaBordadaService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
class EstampaBordadaResourceTest {

    @Inject
    EstampaBordadaResource resource;

    @InjectMock
    EstampaBordadaService service;

    @Test
    void testIncluir() {
        EstampaBordadaDTO dto = new EstampaBordadaDTO(
                "EstampaTeste", "BORDADA", "frente",
                "Descrição teste", "branca", 1
        );

        EstampaBordadaDTOResponse dtoResponse = mock(EstampaBordadaDTOResponse.class);

        when(service.create(dto)).thenReturn(dtoResponse);

        Response response = resource.incluir(dto);

        assertEquals(201, response.getStatus());
        assertEquals(dtoResponse, response.getEntity());
        verify(service).create(dto);
    }

    @Test
    void testAlterar() {
        Long id = 1L;
        EstampaBordadaDTO dto = new EstampaBordadaDTO(
                "EstampaAtualizada", "BORDADA", "costas",
                "Descrição atualizada", "preta", 2
        );

        doNothing().when(service).update(id, dto);

        Response response = resource.alterar(id, dto);

        assertEquals(204, response.getStatus());
        verify(service).update(id, dto);
    }
}
