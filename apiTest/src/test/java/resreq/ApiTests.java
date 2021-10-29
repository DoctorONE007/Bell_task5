package resreq;

import dto.ListResource;
import dto.RegisterInfo;
import dto.UserInfo;
import dto.UserRegister;
import io.restassured.builder.ResponseSpecBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void firstTest() {
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        ListResource listResource = given()
                .when()
                .get("/api/unknown")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(ListResource.class);
        listResource.getData().forEach(data ->
                Assert.assertTrue(data.getColor().matches("^#([a-fA-F0-9]{6})$"), "У поля Color значение является цветом (строка формата «#FFFFFF"));
        Assert.assertEquals((int) listResource.getPerPage(), listResource.getData().size(), "Количество ресурсов в поле data НЕ равна значение per_page");
        Assert.assertTrue(listResource.getData().stream().anyMatch(data -> data.getYear() == 2001));

    }

    @Test
    public void secondTest() {
        UserRegister userRegister = new UserRegister("eve.holt@reqres.in", "pistol");
        Specification.installSpec(Specification.requestSpec(), Specification.responseSpec());
        RegisterInfo registerInfo = given()
                .body(userRegister)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(RegisterInfo.class);

        int id = registerInfo.getId();
        UserInfo userInfo = given()
                .body(userRegister)
                .when()
                .get("/api/user/" + id)
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(UserInfo.class);
        Specification.installSpec(new ResponseSpecBuilder().expectStatusCode(204).build());
        given()
                .when()
                .delete("/api/user/" + id)
                .then()
                .log().body()
                .statusCode(204);
    }

    @Test
    public void thirdTest() {
        UserRegister userRegister = new UserRegister("eve.holt@reqres.in");
        Specification.installSpec(Specification.requestSpec(), new ResponseSpecBuilder().expectStatusCode(400).build());
        System.out.println("\"error\": \"Missing password\"");
        given()
                .body(userRegister)
                .when()
                .post("/api/register")
                .then()
                .log().body()
                .statusCode(400)
                .body(containsString("error"));
    }

}
