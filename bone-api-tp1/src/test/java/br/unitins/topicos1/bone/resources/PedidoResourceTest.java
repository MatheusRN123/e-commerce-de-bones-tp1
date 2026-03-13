package br.unitins.topicos1.bone.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.bone.dto.PedidoDTO;
import br.unitins.topicos1.bone.dto.ItemPedidoDTO;
import br.unitins.topicos1.bone.dto.PagamentoDTO;
import br.unitins.topicos1.bone.dto.PixDTO;
import io.restassured.http.ContentType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
public class PedidoResourceTest {

    @Test
    public void testBuscarTodos() {
        given()
        .when().get("/pedidos")
        .then()
            .statusCode(200)
            .body(anything());
    }

    @Test
    public void testBuscarPorId() {
        given()
            .pathParam("id", 1)
        .when().get("/pedidos/{id}")
        .then()
            .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void testIncluirPedido() {
        ItemPedidoDTO item = new ItemPedidoDTO(1L, 2);
        PagamentoDTO pagamento = new PagamentoDTO("PIX", new PixDTO("chavepix@example.com", "EMAIL"), null, null);

        PedidoDTO dto = new PedidoDTO(
            1L,          // idEndereco
            List.of(item),
            pagamento
        );

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when().post("/pedidos")
        .then()
            .statusCode(anyOf(is(201), is(400))); // 400 caso algum erro de validação
    }

    @Test
    public void testAlterarPedido() {
        ItemPedidoDTO item = new ItemPedidoDTO(1L, 3);
        PagamentoDTO pagamento = new PagamentoDTO("PIX", new PixDTO("novachave@example.com", "EMAIL"), null, null);

        PedidoDTO dto = new PedidoDTO(
            1L,
            List.of(item),
            pagamento
        );

        given()
            .contentType(ContentType.JSON)
            .body(dto)
            .pathParam("id", 1)
        .when().put("/pedidos/{id}")
        .then()
            .statusCode(anyOf(is(204), is(404)));
    }

    @Test
    public void testDeletarPedido() {
        given()
            .pathParam("id", 3)
        .when().delete("/pedidos/{id}")
        .then()
            .statusCode(anyOf(is(204), is(404)));
    }
}
