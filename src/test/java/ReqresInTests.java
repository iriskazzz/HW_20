import org.junit.jupiter.api.Test;

import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class ReqresInTests extends TestBase {

  @Test
  void checkListUsers() {
    given()
            .log().uri()
            .log().method()
            .when()
            .get("/api/users?page=2")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body("total_pages", is(2),
                    "support.url", is("https://reqres.in/#support-heading"));
  }

  @Test
  void checkCreate() {
    given()
            .log().uri()
            .log().method()
            .log().body()
            .body("{\"name\": \"morpheus\",\"job\": \"leader\"}")
            .contentType(JSON)
            .when()
            .post("/api/users")
            .then()
            .log().status()
            .log().body()
            .statusCode(201)
            .body("name", is("morpheus"),
                    "job", is("leader"));
  }

  @Test
  void checkUpdate() {
    given()
            .log().uri()
            .log().method()
            .log().body()
            .body("{\"name\": \"morpheus\",\"job\": \"zion resident\"}")
            .contentType(JSON)
            .when()
            .put("/api/users/2")
            .then()
            .log().status()
            .log().body()
            .statusCode(200)
            .body("name", is("morpheus"),
                    "job", is("zion resident"));
  }

  @Test
  void checkDelete() {
    given()
            .log().uri()
            .log().method()
            .when()
            .delete("/api/users/65")
            .then()
            .log().status()
            .statusCode(204);
  }

  @Test
  void checkLoginUnsuccessful() {
    given()
            .log().uri()
            .log().method()
            .log().body()
            .body("{\"email\": \"peter@klaven\"}")
            .contentType(JSON)
            .when()
            .post("/api/login")
            .then()
            .log().status()
            .log().body()
            .statusCode(400)
            .body("error", is("Missing password"));
  }

}
