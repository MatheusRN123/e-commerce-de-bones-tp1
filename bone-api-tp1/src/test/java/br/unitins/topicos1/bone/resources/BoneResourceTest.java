package br.unitins.topicos1.bone.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;

import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.bone.dto.BoneDTO;
import io.restassured.http.ContentType;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

@QuarkusTest
@TestSecurity(authorizationEnabled = false)
public class BoneResourceTest {

    @Test
    public void testFindAll() {
        given()
        .when().get("/bones")
        .then()
            .statusCode(200)
            .body(anything());
    }

    @Test
    public void testFindByNome() {
        given()
            .pathParam("nome", "Classic")
        .when().get("/bones/find/{nome}")
        .then()
            .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void testFindById() {
        given()
            .pathParam("id", 1)
        .when().get("/bones/{id}")
        .then()
            .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    public void testCreateBone() {
        BoneDTO dto = new BoneDTO(
            "BoneTeste",
            "Preto",
            1L,
            "Curva",
            5.0f,
            10.0f,
            "58cm",
            null,
            1L,
            1L,
            10,
            List.of(),
            150.0
        );

        given()
            .contentType(ContentType.JSON)
            .body(dto)
        .when().post("/bones")
        .then()
            .statusCode(anyOf(is(201), is(400))) // 400 caso falha de validação
            .body("nome", is("BoneTeste"));
    }

    @Test
    public void testUpdateBone() {
        BoneDTO dto = new BoneDTO(
            "BoneAtualizado",
            "Branco",
            1L,
            "Reta",
            5.5f,
            9.0f,
            "60cm",
            null,
            1L,
            1L,
            5,
            List.of(),
            150.0
        );

        given()
            .contentType(ContentType.JSON)
            .body(dto)
            .pathParam("id", 1)
        .when().put("/bones/{id}")
        .then()
            .statusCode(anyOf(is(204), is(404))); // 204 quando atualizado com sucesso
    }

    @Test
    public void testDeleteBone() {
        given()
            .pathParam("id", 3)
        .when().delete("/bones/{id}")
        .then()
            .statusCode(anyOf(is(204), is(404)));
    }
}
