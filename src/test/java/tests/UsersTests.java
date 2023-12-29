package tests;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.requestSpecification;
import static specs.ReqresSpec.responseSpecification;

@Owner("izolina")
@Feature("Actions with users")
public class UsersTests extends TestBase {

  @Test
  @DisplayName("Получение списка пользователей")
  void checkListUsersTest() {
    ListUsersResponseModel response = step("Список пользователей", () ->
            given(requestSpecification)
                    .when()
                    .get("/users?page=2")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(200)
                    .extract().as(ListUsersResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals(2, response.getTotalPages());
      assertEquals("https://reqres.in/#support-heading", response.getSupport().getUrl());
    });
  }

  @Test
  @DisplayName("Проверка создания пользователя")
  void checkCreateTest() {
    CreateUserBodyModel createBody = new CreateUserBodyModel();
    createBody.setName("morpheus");
    createBody.setJob("leader");

    CreateUserResponseModel response = step("Создание пользователя", () ->
            given(requestSpecification)
                    .body(createBody)
                    .when()
                    .post("/users")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(201)
                    .extract().as(CreateUserResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("morpheus", response.getName());
      assertEquals("leader", response.getJob());
    });
  }

  @Test
  @DisplayName("Проверка изменения данных пользователя")
  void checkUpdateTest() {
    CreateUserBodyModel createBody = new CreateUserBodyModel();
    createBody.setName("morpheus");
    createBody.setJob("zion resident");

    CreateUserResponseModel response = step("Изменение данных пользователя", () ->
            given(requestSpecification)
                    .body(createBody)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(200)
                    .extract().as(CreateUserResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("morpheus", response.getName());
      assertEquals("zion resident", response.getJob());
    });
  }

  @Test
  @DisplayName("Проверка удаления пользователя")
  void checkDeleteTest() {
    step("Удаление пользователя", () ->
            given(requestSpecification)
                    .when()
                    .delete("/users/65")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(204)
    );
  }

}
