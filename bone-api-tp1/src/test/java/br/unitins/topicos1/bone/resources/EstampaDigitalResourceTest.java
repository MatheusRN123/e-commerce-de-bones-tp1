package br.unitins.topicos1.bone.resources;

import br.unitins.topicos1.bone.dto.EstampaDigitalDTO;
import br.unitins.topicos1.bone.dto.EstampaDigitalDTOResponse;
import br.unitins.topicos1.bone.resource.EstampaDigitalResource;
import br.unitins.topicos1.bone.service.EstampaDigitalService;
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
class EstampaDigitalResourceUnitTest {

    @Inject
    EstampaDigitalResource resource;

    @InjectMock
    EstampaDigitalService service;

    @Test
    void testIncluir() {
        EstampaDigitalDTO dto = new EstampaDigitalDTO(
                "EstampaDigitalTeste", "DIGITAL", "frente", 
                "Descrição teste", "Alta resolução"
        );

        EstampaDigitalDTOResponse responseMock = mock(EstampaDigitalDTOResponse.class);

        when(service.create(dto)).thenReturn(responseMock);

        Response response = resource.incluir(dto);

        assertEquals(201, response.getStatus());
        assertEquals(responseMock, response.getEntity());
        verify(service).create(dto);
    }

    @Test
    void testAlterar() {
        Long id = 1L;
        EstampaDigitalDTO dto = new EstampaDigitalDTO(
                "EstampaDigitalAtualizada", "DIGITAL", "costas",
                "Descrição atualizada", "4K"
        );

        doNothing().when(service).update(id, dto);

        Response response = resource.alterar(id, dto);

        assertEquals(204, response.getStatus());
        verify(service).update(id, dto);
    }
}
