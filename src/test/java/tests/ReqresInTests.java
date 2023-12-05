package tests;

import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.ReqresSpec.requestSpecification;
import static specs.ReqresSpec.responseSpecification;

public class ReqresInTests extends TestBase {

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
    CreateBodyModel createBody = new CreateBodyModel();
    createBody.setName("morpheus");
    createBody.setJob("leader");

    CreateResponseModel response = step("Создание пользователя", () ->
            given(requestSpecification)
                    .body(createBody)
                    .when()
                    .post("/users")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(201)
                    .extract().as(CreateResponseModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("morpheus", response.getName());
      assertEquals("leader", response.getJob());
    });
  }

  @Test
  @DisplayName("Проверка изменения данных пользователя")
  void checkUpdateTest() {
    CreateBodyModel createBody = new CreateBodyModel();
    createBody.setName("morpheus");
    createBody.setJob("zion resident");

    CreateResponseModel response = step("Изменение данных пользователя", () ->
            given(requestSpecification)
                    .body(createBody)
                    .when()
                    .put("/users/2")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(200)
                    .extract().as(CreateResponseModel.class)
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

  @Test
  @DisplayName("Проверка неуспешной авторизации без пароля")
  void checkLoginUnsuccessfulTest() {
    LoginBodyModel loginBody = new LoginBodyModel();
    loginBody.setEmail("peter@klaven");

    ErrorModel response = step("Вызов метода авторищации", () ->
            given(requestSpecification)
                    .body(loginBody)
                    .when()
                    .post("/login")
                    .then()
                    .spec(responseSpecification)
                    .statusCode(400)
                    .extract().as(ErrorModel.class)
    );

    step("Проверка данных из ответа", () -> {
      assertEquals("Missing password", response.getError());
    });
  }

}
